package org.meristem.oneapp.walletservice.services.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.walletservice.constants.AppConstants;
import org.meristem.oneapp.walletservice.domains.enums.ProviderCode;
import org.meristem.oneapp.walletservice.domains.requests.BankAccountRequest;
import org.meristem.oneapp.walletservice.domains.requests.BankDetailsQueryRequest;
import org.meristem.oneapp.walletservice.domains.responses.BankAccountResponse;
import org.meristem.oneapp.walletservice.domains.responses.BankCodeResponse;
import org.meristem.oneapp.walletservice.domains.responses.BvnQueryResponse;
import org.meristem.oneapp.walletservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.walletservice.exception.exceptions.ResourceNotFoundException;
import org.meristem.oneapp.walletservice.integrations.PaystackClient;
import org.meristem.oneapp.walletservice.integrations.SmileIdClient;
import org.meristem.oneapp.walletservice.integrations.requests.SmileIdEnhancedKycRequest;
import org.meristem.oneapp.walletservice.integrations.responses.AccountQueryResult;
import org.meristem.oneapp.walletservice.integrations.responses.SmileIdWebhookNotification;
import org.meristem.oneapp.walletservice.repositories.BanksRepository;
import org.meristem.oneapp.walletservice.services.IBankService;
import org.meristem.oneapp.walletservice.utils.AppUtil;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankService implements IBankService {

    private final BanksRepository banksRepository;
    private final PaystackClient paystackClient;
    private final SmileIdClient smileIdClient;
    public static final String ID_APPROVED_STATUS = "1012";

    List<String> errorCodes = List.of("1013", "1014");


    public BankAccountResponse resolveAccount(BankAccountRequest request) {

        AccountQueryResult result = paystackClient.getBalance(request.bankCode(), request.accountNumber());
        if (result.status()) {
            return new BankAccountResponse(AppUtil.titleCase(result.data().accountName()));
        }
        throw new BadRequestException("Unable to resolve account");
    }

    public List<BankCodeResponse> getBanks(ProviderCode providerCode) {
        return banksRepository.findBanksByProviderCode(providerCode.getValue());
    }



    @Override
    public BvnQueryResponse bankDetailsQuery(BankDetailsQueryRequest request) {


        SmileIdEnhancedKycRequest smileIdEnhancedKycRequest = SmileIdEnhancedKycRequest.builder()
                .idNumber(request.idNumber())
                .country(request.country()).idType(AppConstants.BANK_ACCOUNT)
                .bankCode(request.bankCode()).build();
        SmileIdWebhookNotification response = smileIdClient.enhancedBvnQuery(smileIdEnhancedKycRequest);
        if (ID_APPROVED_STATUS.equals(response.getResultCode()) && confirmSignature(response.getSignature(), response.getTimestamp())) {

            return BvnQueryResponse.builder().middleName(response.getMiddleName())
                    .email(response.getEmail()).firstName(response.getFirstName())
                    .lastName(response.getLastName())
                    .phoneNumber(response.getPhoneNumber())
                    .build();
        } else if (errorCodes.contains(response.getResultCode())) {
            throw new ResourceNotFoundException("Enter a valid bvn", AppConstants.BANK_ACCOUNT, request.idNumber());
        } else {
            throw new BadRequestException("Try again later.");
        }
    }

    /**
     * Confirms the validity of a received signature by comparing it with a generated signature.
     *
     * @param receivedSignature The signature received in the request.
     * @param receivedTimestamp The timestamp received in the request.
     * @return True if the signature is valid, false otherwise.
     */
    private boolean confirmSignature(String receivedSignature, String receivedTimestamp) {

        try {
            Mac mac = getMac(receivedTimestamp);
            String generatedSignature = Base64.getEncoder().encodeToString(mac.doFinal());
            return generatedSignature.equals(receivedSignature);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
