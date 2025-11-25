package org.meristem.oneapp.cloudgateway.controllers;


import org.meristem.oneapp.cloudgateway.domain.response.FallbackResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackController {

    @RequestMapping("/fallback")
    public ResponseEntity<FallbackResponse> fallback() {
        return new ResponseEntity<>(new FallbackResponse("Service is temporarily unavailable. Please try again later."),
                HttpStatus.SERVICE_UNAVAILABLE);
    }
}
