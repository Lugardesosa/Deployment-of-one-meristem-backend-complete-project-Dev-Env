package org.meristem.oneapp.usersservice.utils;


import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.exception.exceptions.CryptoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

@Slf4j
@Component
public class EncryptionUtil {

    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    private static final String KDF_ALGORITHM = "PBKDF2WithHmacSHA256";

    private static final int KEY_SIZE = 256;
    private static final int ITERATIONS = 310_000;
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;
    private static final int SALT_LENGTH = 16;
    private static final byte KEY_VERSION = 1;

    private static final SecureRandom RANDOM = new SecureRandom();

    private final char[] masterSecret;

    public EncryptionUtil(@Value("${encryption.secret-key}") String SECRET_KEY) {
        this.masterSecret = SECRET_KEY.toCharArray();
    }

    public String encrypt(String plainText) {

        try {
            byte[] salt = randomBytes(SALT_LENGTH);
            byte[] iv = randomBytes(IV_LENGTH);

            SecretKey aesKey = deriveKey(salt);

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

            byte[] cipherText = cipher.doFinal(
                    plainText.getBytes(StandardCharsets.UTF_8)
            );

            ByteBuffer buffer = ByteBuffer.allocate(
                    1 + salt.length + iv.length + cipherText.length
            );

            buffer.put(KEY_VERSION);
            buffer.put(salt);
            buffer.put(iv);
            buffer.put(cipherText);

            return Base64.getEncoder().encodeToString(buffer.array());

        } catch (Exception e) {
            throw new CryptoException("Encryption failed", e);
        }
    }

    public String decrypt(String encrypted) {

        try {
            byte[] decoded = Base64.getDecoder().decode(encrypted);
            ByteBuffer buffer = ByteBuffer.wrap(decoded);

            byte version = buffer.get();
            if (version != KEY_VERSION) {
                throw new CryptoException("Unsupported key version: " + version);
            }

            byte[] salt = new byte[SALT_LENGTH];
            buffer.get(salt);

            byte[] iv = new byte[IV_LENGTH];
            buffer.get(iv);

            byte[] cipherText = new byte[buffer.remaining()];
            buffer.get(cipherText);

            SecretKey aesKey = deriveKey(salt);

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, aesKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

            byte[] plainText = cipher.doFinal(cipherText);

            return new String(plainText, StandardCharsets.UTF_8);

        } catch (AEADBadTagException e) {
            throw new CryptoException("Data tampered or wrong key", e);
        } catch (Exception e) {
            throw new CryptoException("Decryption failed", e);
        }
    }


    private SecretKey deriveKey(byte[] salt) throws Exception {

        KeySpec spec = new PBEKeySpec(masterSecret, salt, ITERATIONS, KEY_SIZE);

        SecretKeyFactory factory = SecretKeyFactory.getInstance(KDF_ALGORITHM);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();

        return new SecretKeySpec(keyBytes, "AES");
    }

    private static byte[] randomBytes(int length) {
        byte[] bytes = new byte[length];
        RANDOM.nextBytes(bytes);
        return bytes;
    }
}
