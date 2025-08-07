package org.meristem.oneapp.coreservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AlternateAssetType {

    CRYPTO_NFT("Cryptocurrency & NFT"),
    DIGITAL_PLATFORM("Digital Platform");

    private final String description;
}
