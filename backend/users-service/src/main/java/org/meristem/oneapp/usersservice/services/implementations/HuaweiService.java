package org.meristem.oneapp.usersservice.services.implementations;

import com.obs.services.ObsClient;
import com.obs.services.model.HttpMethodEnum;
import com.obs.services.model.TemporarySignatureRequest;
import com.obs.services.model.TemporarySignatureResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.meristem.oneapp.usersservice.config.configProperties.HuaweiConfigProperties;
import org.meristem.oneapp.usersservice.domains.enums.FileType;
import org.meristem.oneapp.usersservice.domains.enums.SignedUrlType;
import org.meristem.oneapp.usersservice.domains.requests.SignedUrlRequest;
import org.meristem.oneapp.usersservice.domains.responses.SignedUrlResponse;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.repositories.FilesRepository;
import org.meristem.oneapp.usersservice.services.IHuaweiService;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Service for generating temporary signed URLs for Huawei Object Storage (OBS).
 * <p>
 * This service builds object keys and delegates to Huawei OBS to create
 * temporary signatures for secure, time-limited access to objects.
 * It supports creating URLs for both uploads (PUT) and downloads (GET),
 * and selects the target bucket based on the requested file type.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HuaweiService implements IHuaweiService {

    private final HuaweiConfigProperties huaweiConfigProperties;
    private final FilesRepository filesRepository;

    /**
     * Generates a temporary signed URL for uploading or downloading a file in Huawei OBS.
     * <p>
     * Rules:
     * - GET: uses the provided {@code fileName} as the existing object key for download.
     * - PUT: requires {@code contentType}, generates a unique object key using the current user and timestamp,
     *   and sets the {@code Content-Type} header for the upload.
     * The target bucket is chosen based on the file type in the request, and the URL TTL is taken
     * from configuration.
     *
     * @param signedUrlRequest request parameters describing desired HTTP method, file name, file type, and content type (for PUT)
     * @return a response containing the signed URL and resolved object key, along with metadata
     * @throws BadRequestException if required data is missing or the signed URL cannot be generated
     */
    public SignedUrlResponse getSignedUrl(SignedUrlRequest signedUrlRequest) {

        try (ObsClient obsClient = new ObsClient(huaweiConfigProperties.accessKeyId(), huaweiConfigProperties.accessSecretId(), huaweiConfigProperties.obsEndpoint())) {

            String userEmail = (AppUtil.getLoggedInUserEmail().split("@")[0]).replaceAll("[!#$%&'*+-/=?^_`{|}~]", "");

            TemporarySignatureRequest request = new TemporarySignatureRequest(signedUrlRequest.method(), huaweiConfigProperties.signedUrlTtlSec());

            String objectKey = signedUrlRequest.method() == HttpMethodEnum.GET ? signedUrlRequest.fileName() : userEmail.concat("-").concat(String.valueOf(System.currentTimeMillis())).concat("-").concat(signedUrlRequest.fileName().replaceAll("\\.\\w+$|\\.\\w+\\s+", ""));

            String bucketName = List.of(SignedUrlType.IMAGE, SignedUrlType.PROFILE_PICTURE).contains(signedUrlRequest.type()) ? huaweiConfigProperties.imagesBucketName() : huaweiConfigProperties.documentBucketName();
            request.setBucketName(bucketName);
            request.setObjectKey(objectKey);

            if (SignedUrlType.PROFILE_PICTURE.equals(signedUrlRequest.type())) {
                filesRepository.findByFileTypeAndUserId(FileType.PROFILE_PICTURE.getValue(), AppUtil.getLoggedInUserId())
                        .ifPresent(i -> request.setObjectKey(i.getFileKey()));
                objectKey = request.getObjectKey();
            }

            if (HttpMethodEnum.PUT == signedUrlRequest.method()) {
                if (signedUrlRequest.contentType() == null) {
                    throw new BadRequestException("Content type is required");
                }
                request.setHeaders(Map.of(HttpHeaders.CONTENT_TYPE, signedUrlRequest.contentType()));
            }

            TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
            return SignedUrlResponse.builder().signedUrl(response.getSignedUrl()).fileKey(objectKey).contentType(signedUrlRequest.contentType())
                    .fileType(signedUrlRequest.type().getValue()).build();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BadRequestException("Signed key url could not be generated");
        }

    }
}
