package org.meristem.oneapp.usersservice.config.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
public record RsaKeys(RSAPublicKey publicKey, RSAPrivateKey privateKey) {}