package org.meristem.oneapp.notificationservice.dtos.configs;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BufferingClientHttpResponseWrapper implements ClientHttpResponse {

    private final ClientHttpResponse delegate;
    private final byte[] body;

    public BufferingClientHttpResponseWrapper(ClientHttpResponse delegate, byte[] body) {
        this.delegate = delegate;
        this.body = body;
    }

    @NonNull
    @Override
    public HttpStatusCode getStatusCode() throws IOException {
        return delegate.getStatusCode();
    }

    @NonNull
    @Override
    public String getStatusText() throws IOException {
        return delegate.getStatusText();
    }

    @Override
    public void close() {
        delegate.close();

    }

    @NonNull
    @Override
    public InputStream getBody() throws IOException {
        return new ByteArrayInputStream(body);
    }

    @NonNull
    @Override
    public HttpHeaders getHeaders() {
        return delegate.getHeaders();
    }
}
