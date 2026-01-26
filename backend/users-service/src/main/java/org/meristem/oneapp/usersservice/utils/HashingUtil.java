package org.meristem.oneapp.usersservice.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Component
public class HashingUtil {

    public String hmacWithSha256(String key, String plainText) {

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec =
                    new SecretKeySpec(key.getBytes(), "HmacSHA256");

            mac.init(keySpec);
            byte[] hash = mac.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Unable to hash plaintext");
        }
    }
}
