package org.meristem.oneapp.usersservice.services;

import com.obs.services.ObsClient;
import com.obs.services.model.HttpMethodEnum;
import com.obs.services.model.TemporarySignatureRequest;
import com.obs.services.model.TemporarySignatureResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.meristem.oneapp.usersservice.config.configProperties.HuaweiConfigProperties;
import org.meristem.oneapp.usersservice.domains.enums.SignedUrlType;
import org.meristem.oneapp.usersservice.domains.requests.SignedUrlRequest;
import org.meristem.oneapp.usersservice.domains.responses.SignedUrlResponse;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class HuaweiService {

    private final HuaweiConfigProperties huaweiConfigProperties;

    public SignedUrlResponse getSignedUrl(SignedUrlRequest signedUrlRequest) {

        try (ObsClient obsClient = new ObsClient(huaweiConfigProperties.accessKeyId(), huaweiConfigProperties.accessSecretId(), huaweiConfigProperties.obsEndpoint())) {

            String userEmail = (AppUtil.getLoggedInUserEmail().split("@")[0]).replaceAll("[!#$%&'*+-/=?^_`{|}~]", "");

            TemporarySignatureRequest request = new TemporarySignatureRequest(signedUrlRequest.method(), huaweiConfigProperties.signedUrlTtlSec());

            String objectKey = signedUrlRequest.method() == HttpMethodEnum.GET ? signedUrlRequest.fileName() : userEmail.concat("-").concat(String.valueOf(System.currentTimeMillis())).concat("-").concat(signedUrlRequest.fileName().replaceAll("\\.\\w+$|\\.\\w+\\s+", ""));

            String bucketName = signedUrlRequest.type() == SignedUrlType.IMAGE ? huaweiConfigProperties.imagesBucketName() : huaweiConfigProperties.documentBucketName();
            request.setBucketName(bucketName);
            request.setObjectKey(objectKey);

            if (HttpMethodEnum.PUT == signedUrlRequest.method()) {
                if (signedUrlRequest.contentType() == null) {
                    throw new BadRequestException("Content type is required");
                }
                request.setHeaders(Map.of(HttpHeaders.CONTENT_TYPE, signedUrlRequest.contentType()));
            }

            TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
            return SignedUrlResponse.builder().signedUrl(response.getSignedUrl()).imageKey(objectKey).contentType(signedUrlRequest.contentType()).build();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BadRequestException("Signed key url could not be generated");
        }

    }
}
