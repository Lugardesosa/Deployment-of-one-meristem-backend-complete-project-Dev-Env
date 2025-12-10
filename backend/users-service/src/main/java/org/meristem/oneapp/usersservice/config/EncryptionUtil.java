package org.meristem.oneapp.usersservice.config;


import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

@Slf4j
@Component
public class EncryptionUtil {

    public static final String SHA_CRYPT = "SHA-256";

    @Value("${encryption.algorithm}")
    private String AES_ALGORITHM;
    @Value("${encryption.algorithm_gcm}")
    private String AES_ALGORITHM_GCM;

    @Value("${encryption.iv.size}")
    private Integer IV_LENGTH_ENCRYPT;
    public static final Integer TAG_LENGTH_ENCRYPT = 16;

    @Value("${encryption.salt}")
    private String SALT;

    public @Nullable String encrypt(String plainText)  {

        try {

            byte[] iv = new byte[IV_LENGTH_ENCRYPT];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);

            SecretKeySpec aesKey = generateAesKeyFromPassphrase();

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM_GCM);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_ENCRYPT * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, gcmSpec);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

            byte[] combinedIvAndCipherText = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, combinedIvAndCipherText, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, combinedIvAndCipherText, iv.length, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(combinedIvAndCipherText);
        }  catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public @Nullable String decrypt(String cipherText) {

        try {
            byte[] decodedCipherText = Base64.getDecoder().decode(cipherText);

            SecretKeySpec aesKey = generateAesKeyFromPassphrase();

            byte[] iv = new byte[IV_LENGTH_ENCRYPT];
            System.arraycopy(decodedCipherText, 0, iv, 0, iv.length);
            byte[] encryptedText = new byte[decodedCipherText.length - IV_LENGTH_ENCRYPT];
            System.arraycopy(decodedCipherText, IV_LENGTH_ENCRYPT, encryptedText, 0, encryptedText.length);

            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_ENCRYPT * 8, iv);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM_GCM);
            cipher.init(Cipher.DECRYPT_MODE, aesKey, gcmSpec);

            byte[] decryptedBytes = cipher.doFinal(encryptedText);

            return new String(decryptedBytes);
        }  catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private SecretKeySpec generateAesKeyFromPassphrase() throws Exception {
        MessageDigest sha256 = MessageDigest.getInstance(SHA_CRYPT);
        byte[] keyBytes = sha256.digest(SALT.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, AES_ALGORITHM);
    }
}
