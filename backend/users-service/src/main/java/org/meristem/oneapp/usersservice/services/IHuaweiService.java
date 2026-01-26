package org.meristem.oneapp.usersservice.services;

import org.meristem.oneapp.usersservice.domains.requests.SignedUrlRequest;
import org.meristem.oneapp.usersservice.domains.responses.SignedUrlResponse;

/**
 * Interface for generating temporary signed URLs for Huawei Object Storage (OBS).
 * Provides functionality for creating secure, time-limited access URLs for objects.
 */
public interface IHuaweiService {

    /**
     * Generates a temporary signed URL for uploading or downloading a file in Huawei OBS.
     *
     * @param signedUrlRequest request parameters describing desired HTTP method, file name, file type, and content type
     * @return a response containing the signed URL and resolved object key, along with metadata
     */
    SignedUrlResponse getSignedUrl(SignedUrlRequest signedUrlRequest);
}
