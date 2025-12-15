package org.meristem.oneapp.cloudgateway.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jspecify.annotations.NullMarked;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter;
import org.springframework.cloud.gateway.filter.headers.TrustedProxies;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;


public class SafeXForwardedHeadersFilter implements HttpHeadersFilter, Ordered {

    /**
     * Default http port.
     */
    public static final int HTTP_PORT = 80;
    /**
     * Default https port.
     */
    public static final int HTTPS_PORT = 443;
    /**
     * Http url scheme.
     */
    public static final String HTTP_SCHEME = "http";
    /**
     * Https url scheme.
     */
    public static final String HTTPS_SCHEME = "https";
    /**
     * X-Forwarded-For Header.
     */
    public static final String X_FORWARDED_FOR_HEADER = "X-Forwarded-For";
    /**
     * X-Forwarded-Host Header.
     */
    public static final String X_FORWARDED_HOST_HEADER = "X-Forwarded-Host";
    /**
     * X-Forwarded-Port Header.
     */
    public static final String X_FORWARDED_PORT_HEADER = "X-Forwarded-Port";
    /**
     * X-Forwarded-Proto Header.
     */
    public static final String X_FORWARDED_PROTO_HEADER = "X-Forwarded-Proto";
    /**
     * X-Forwarded-Prefix Header.
     */
    public static final String X_FORWARDED_PREFIX_HEADER = "X-Forwarded-Prefix";
    private static final Log log = LogFactory.getLog(SafeXForwardedHeadersFilter.class);
    private final TrustedProxies trustedProxies;
    /**
     * The order of the XForwardedHeadersFilter.
     */
    private int order = 0;
    /**
     * If the XForwardedHeadersFilter is enabled.
     */
    private boolean enabled = true;
    /**
     * If X-Forwarded-For is enabled.
     */
    private boolean forEnabled = true;
    /**
     * If X-Forwarded-Host is enabled.
     */
    private boolean hostEnabled = true;
    /**
     * If X-Forwarded-Port is enabled.
     */
    private boolean portEnabled = true;
    /**
     * If X-Forwarded-Proto is enabled.
     */
    private boolean protoEnabled = true;
    /**
     * If X-Forwarded-Prefix is enabled.
     */
    private boolean prefixEnabled = true;
    /**
     * If appending X-Forwarded-For as a list is enabled.
     */
    private boolean forAppend = true;
    /**
     * If appending X-Forwarded-Host as a list is enabled.
     */
    private boolean hostAppend = true;
    /**
     * If appending X-Forwarded-Port as a list is enabled.
     */
    private boolean portAppend = true;
    /**
     * If appending X-Forwarded-Proto as a list is enabled.
     */
    private boolean protoAppend = true;
    /**
     * If appending X-Forwarded-Prefix as a list is enabled.
     */
    private boolean prefixAppend = true;

    @Deprecated
    public SafeXForwardedHeadersFilter() {
        trustedProxies = s -> true;
        log.warn(GatewayProperties.PREFIX + ".trusted-proxies is not set. Using deprecated Constructor. Untrusted hosts might be added to Forwarded header.");
    }

    public SafeXForwardedHeadersFilter(String trustedProxiesRegex) {
        trustedProxies = TrustedProxies.from(trustedProxiesRegex);
    }

    private static String substringBeforeLast(String str, String separator) {
        if (ObjectUtils.isEmpty(str) || ObjectUtils.isEmpty(separator)) {
            return str;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    @Override
    public @NullMarked HttpHeaders filter(HttpHeaders input, ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();

        HttpHeaders updated = new HttpHeaders();
        // get remote address.
        String remoteAddr = null;
        if (request.getRemoteAddress() != null && request.getRemoteAddress().getAddress() != null) {
            remoteAddr = request.getRemoteAddress().getHostString();
        }
        // trusted default true
        boolean isTrusted = true;

        for (Map.Entry<String, List<String>> entry : input.headerSet()) {
            updated.addAll(entry.getKey(), entry.getValue());
        }

        if (isForEnabled()) {
            //check trusted proxies only contains X-Forwarded-For
            if (input.containsKey(X_FORWARDED_FOR_HEADER)) {
                remoteAddr = isNull(remoteAddr) ? "" : remoteAddr;
                isTrusted = trustedProxies.isTrusted(remoteAddr);
            }
            // match x-forwarded for against trusted proxies
            write(updated, X_FORWARDED_FOR_HEADER, remoteAddr, isForAppend() && isTrusted);
        }

        String proto = request.getURI().getScheme();
        if (isProtoEnabled()) {
            write(updated, X_FORWARDED_PROTO_HEADER, proto, isProtoAppend() && isTrusted);
        }

        if (isPrefixEnabled()) {
            // If the path of the url that the gw is routing to is a subset
            // (and ending part) of the url that it is routing from then the difference
            // is the prefix e.g. if request original.com/prefix/get/ is routed
            // to routedservice:8090/get then /prefix is the prefix
            // - see XForwardedHeadersFilterTests, so first get uris, then extract paths
            // and remove one from another if it's the ending part.

            LinkedHashSet<URI> originalUris = exchange.getAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
            URI requestUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);

            if (originalUris != null && requestUri != null) {

                originalUris.forEach(originalUri -> {

                    if (originalUri.getPath() != null) {
                        String prefix = originalUri.getPath();

                        // strip trailing slashes before checking if request path is end
                        // of original path
                        String originalUriPath = stripTrailingSlash(originalUri);
                        String requestUriPath = stripTrailingSlash(requestUri);

                        updateRequest(updated, originalUri, originalUriPath, requestUriPath);

                    }
                });
            }
        }

        if (isPortEnabled()) {
            String port = String.valueOf(request.getURI().getPort());
            if (request.getURI().getPort() < 0) {
                port = String.valueOf(getDefaultPort(proto));
            }
            write(updated, X_FORWARDED_PORT_HEADER, port, isPortAppend() && isTrusted);
        }

        if (isHostEnabled()) {
            String host = toHostHeader(request);
            write(updated, X_FORWARDED_HOST_HEADER, host, isHostAppend() && isTrusted);
        }

        return updated;
    }

    private void updateRequest(HttpHeaders updated, URI originalUri, String originalUriPath, String requestUriPath) {
        String prefix;
        if (requestUriPath != null && (originalUriPath.endsWith(requestUriPath))) {
            prefix = substringBeforeLast(originalUriPath, requestUriPath);
            if (prefix != null && !prefix.isEmpty() && prefix.length() <= originalUri.getPath().length()) {
                write(updated, X_FORWARDED_PREFIX_HEADER, prefix, isPrefixAppend());
            }
        }
    }

    private void write(HttpHeaders headers, String name, String value, boolean append) {
        if (value == null) {
            return;
        }
        if (append) {
            headers.add(name, value);
        } else {
            headers.set(name, value);
        }
    }

    private int getDefaultPort(String scheme) {
        return HTTPS_SCHEME.equals(scheme) ? HTTPS_PORT : HTTP_PORT;
    }

    private String toHostHeader(ServerHttpRequest request) {
        int port = request.getURI().getPort();
        String host = request.getURI().getHost();
        String scheme = request.getURI().getScheme();
        if (port < 0 || (port == HTTP_PORT && HTTP_SCHEME.equals(scheme)) || (port == HTTPS_PORT && HTTPS_SCHEME.equals(scheme))) {
            return host;
        } else {
            return host + ":" + port;
        }
    }

    private String stripTrailingSlash(URI uri) {
        if (uri.getPath().endsWith("/")) {
            return uri.getPath().substring(0, uri.getPath().length() - 1);
        } else {
            return uri.getPath();
        }
    }

    public TrustedProxies getTrustedProxies() {
        return trustedProxies;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isForEnabled() {
        return forEnabled;
    }

    public void setForEnabled(boolean forEnabled) {
        this.forEnabled = forEnabled;
    }

    public boolean isHostEnabled() {
        return hostEnabled;
    }

    public void setHostEnabled(boolean hostEnabled) {
        this.hostEnabled = hostEnabled;
    }

    public boolean isPortEnabled() {
        return portEnabled;
    }

    public void setPortEnabled(boolean portEnabled) {
        this.portEnabled = portEnabled;
    }

    public boolean isProtoEnabled() {
        return protoEnabled;
    }

    public void setProtoEnabled(boolean protoEnabled) {
        this.protoEnabled = protoEnabled;
    }

    public boolean isPrefixEnabled() {
        return prefixEnabled;
    }

    public void setPrefixEnabled(boolean prefixEnabled) {
        this.prefixEnabled = prefixEnabled;
    }

    public boolean isForAppend() {
        return forAppend;
    }

    public void setForAppend(boolean forAppend) {
        this.forAppend = forAppend;
    }

    public boolean isHostAppend() {
        return hostAppend;
    }

    public void setHostAppend(boolean hostAppend) {
        this.hostAppend = hostAppend;
    }

    public boolean isPortAppend() {
        return portAppend;
    }

    public void setPortAppend(boolean portAppend) {
        this.portAppend = portAppend;
    }

    public boolean isProtoAppend() {
        return protoAppend;
    }

    public void setProtoAppend(boolean protoAppend) {
        this.protoAppend = protoAppend;
    }

    public boolean isPrefixAppend() {
        return prefixAppend;
    }

    public void setPrefixAppend(boolean prefixAppend) {
        this.prefixAppend = prefixAppend;
    }
}
