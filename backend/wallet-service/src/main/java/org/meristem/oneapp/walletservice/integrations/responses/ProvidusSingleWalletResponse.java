package org.meristem.oneapp.walletservice.integrations.responses;


public record ProvidusSingleWalletResponse(
        boolean status,
        Wallet wallet
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
            String accountReference,
            int minBalance,
            int maxBalance,
            int dailyTransactionLimit
    ) {}
}