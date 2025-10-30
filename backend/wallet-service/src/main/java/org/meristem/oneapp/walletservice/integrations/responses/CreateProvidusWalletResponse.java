package org.meristem.oneapp.walletservice.integrations.responses;

import java.math.BigDecimal;
import java.util.Map;

public record CreateProvidusWalletResponse(
        boolean status,
        Customer customer,
        Wallet wallet
) {
    public record Customer(
            String id,
            String tier,
            String bvn,
            String dateOfBirth,
            String phoneNumber,
            String currency,
            String email,
            String lastName,
            String firstName,
            String address,
            String BVNLastName,
            String BVNFirstName,
            boolean nameMatch,
            String mode,
            String MerchantId,
            String updatedAt,
            String createdAt,
            String deletedAt,
            Map<String, String> metadata
    ) { }

    public record Wallet(
            String id,
            String mode,
            String email,
            String status,
            String bankName,
            String bankCode,
            String walletId,
            String walletType,
            String accountName,
            String accountNumber,
            BigDecimal bookedBalance,
            BigDecimal availableBalance,
            String accountReference
    ) {}
}