package org.meristem.oneapp.walletservice.integrations.requests;


public record NameInquiryRequest(String destinationBankCode, String destinationAccountNumber) {
}
