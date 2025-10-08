package org.meristem.oneapp.walletservice.integrations.responses;

import java.util.List;

public record ProvidusWalletsResponse (
        boolean status,
        List<Wallet> wallets,
        Metadata metadata
) {
    public record Wallet(
            String id,
            String type,
            String tier,
            String status,
            String email,
            String customerId,
            String lastName,
            String firstName,
            String bankName,
            String bankCode,
            String createdAt,
            String updatedAt,
            String accountName,
            String phoneNumber,
            boolean postNoCredit,
            String accountNumber,
            int bookedBalance,
            int availableBalance,
            String accountReference
    ) {}

    public record Metadata(
            int page,
            int totalRecords,
            int totalPages
    ) {}
}