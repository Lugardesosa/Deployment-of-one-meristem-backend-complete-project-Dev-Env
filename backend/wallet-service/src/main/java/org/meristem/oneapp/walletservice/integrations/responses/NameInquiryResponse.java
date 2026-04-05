package org.meristem.oneapp.walletservice.integrations.responses;

public record NameInquiryResponse(

        String status,
        String statusMessage,
        String nameOnAccount,
        String accountNumber,
        String bankCode,
        String bvn,
        String bvnAccountNo,
        String bvnPhoneNo,
        String phoneNumber,
        Boolean valid,
        Integer avgProcessingTimeMs
) {
}
