package org.meristem.oneapp.usersservice.config.configProperties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "huawei")
public record HuaweiConfigProperties(String accessKeyId, String accessSecretId, String obsEndpoint,
                                     Long signedUrlTtlSec, String imagesBucketName, String documentBucketName) {
}
