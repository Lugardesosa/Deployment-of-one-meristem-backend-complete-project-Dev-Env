package org.meristem.oneapp.trustiesservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AlternateAssetType {

    CRYPTO_NFT("Cryptocurrency & NFT"),
    DIGITAL_PLATFORM("Digital Platform"),
    FINTECH_WALLETS("Fintech Wallets"),;

    private final String description;
}
