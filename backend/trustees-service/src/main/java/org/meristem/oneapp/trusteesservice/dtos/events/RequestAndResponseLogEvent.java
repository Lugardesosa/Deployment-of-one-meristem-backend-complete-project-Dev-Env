package org.meristem.oneapp.trusteesservice.dtos.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

@Getter
public class RequestAndResponseLogEvent extends ApplicationEvent {

    private final byte[] requestBody;
    private final byte[] responseBody;
    private final Long duration;
    private final String requestURI;
    private final String method;
    private final Map<String, String[]> parameters;
    private final Map<String, Object> headers;
    private final int status;


    public RequestAndResponseLogEvent(Object source, byte[] requestBody, byte[] responseBody, Long duration, String requestURI, String method, Map<String, String[]> parameters, Map<String, Object> headers, int status) {
        super(source);
        this.requestBody = requestBody;
        this.responseBody = responseBody;
        this.duration = duration;
        this.requestURI = requestURI;
        this.method = method;
        this.parameters = parameters;
        this.headers = headers;
        this.status = status;
    }
}
