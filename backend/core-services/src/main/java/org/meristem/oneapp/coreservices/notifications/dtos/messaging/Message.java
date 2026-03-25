package org.meristem.oneapp.coreservices.notifications.dtos.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meristem.oneapp.coreservices.notifications.domains.enums.EmailTemplate;
import org.springframework.core.io.InputStreamSource;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    private String subject;
    private String body;
    private String[] recipient;
    private String[] cc;
    @Builder.Default
    private Map<String, File> files = new HashMap<>();
    @Builder.Default
    private Map<String, InputStreamSource> inputStreamSourceMap = new HashMap<>();

    private EmailTemplate emailTemplate;
    private Map<String, Object> context;
}