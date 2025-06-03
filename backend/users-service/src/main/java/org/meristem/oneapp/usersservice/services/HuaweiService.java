package org.meristem.oneapp.usersservice.services;

import com.obs.services.ObsClient;
import com.obs.services.model.TemporarySignatureRequest;
import com.obs.services.model.TemporarySignatureResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.config.configProperties.HuaweiConfigProperties;
import org.meristem.oneapp.usersservice.domains.requests.SignedUrlRequest;
import org.meristem.oneapp.usersservice.domains.responses.SignedUrlResponse;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class HuaweiService {

    private final HuaweiConfigProperties huaweiConfigProperties;

    public SignedUrlResponse getSignedUrl(SignedUrlRequest signedUrlRequest) {


        try (ObsClient obsClient = new ObsClient(huaweiConfigProperties.accessKeyId(), huaweiConfigProperties.accessSecretId(), huaweiConfigProperties.obsEndpoint())) {

            String userEmail = AppUtil.getLoggedInUserEmail().split("@")[0];

            TemporarySignatureRequest request = new TemporarySignatureRequest(signedUrlRequest.method(), huaweiConfigProperties.signedUrlTtlSec());

            String objectKey = userEmail.concat(String.valueOf(System.currentTimeMillis())).concat(signedUrlRequest.fileName());

            request.setBucketName(huaweiConfigProperties.imagesBucketName());
            request.setObjectKey(objectKey);

            UriComponents uriComponents = UriComponentsBuilder.fromUriString(huaweiConfigProperties.obsEndpoint())
                    .path(objectKey).build();
            TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
            return SignedUrlResponse.builder().signedUrl(response.getSignedUrl()).uploadUrl(uriComponents.toString()).build();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BadRequestException("Signed key url could not be generated");
        }

    }
}
