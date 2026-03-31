package org.meristem.oneapp.walletservice.services.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.walletservice.config.configProperties.SmileIdProperties;
import org.meristem.oneapp.walletservice.constants.AppConstants;
import org.meristem.oneapp.walletservice.domains.enums.ProviderCode;
import org.meristem.oneapp.walletservice.domains.requests.AddAccountRequest;
import org.meristem.oneapp.walletservice.domains.requests.BankAccountRequest;
import org.meristem.oneapp.walletservice.domains.requests.BankDetailsQueryRequest;
import org.meristem.oneapp.walletservice.domains.responses.BankAccountResponse;
import org.meristem.oneapp.walletservice.domains.responses.BankCodeResponse;
import org.meristem.oneapp.walletservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.walletservice.exception.exceptions.ResourceNotFoundException;
import org.meristem.oneapp.walletservice.integrations.PaystackClient;
import org.meristem.oneapp.walletservice.integrations.SmileIdClient;
import org.meristem.oneapp.walletservice.integrations.requests.SmileIdEnhancedKycRequest;
import org.meristem.oneapp.walletservice.integrations.responses.AccountQueryResult;
import org.meristem.oneapp.walletservice.integrations.responses.SmileIdWebhookNotification;
import org.meristem.oneapp.walletservice.integrations.responses.UpdateResponse;
import org.meristem.oneapp.walletservice.repositories.BanksRepository;
import org.meristem.oneapp.walletservice.services.IBankService;
import org.meristem.oneapp.walletservice.utils.AppUtil;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankService implements IBankService {

    private final BanksRepository banksRepository;
    private final PaystackClient paystackClient;
    private final SmileIdClient smileIdClient;
    private final SmileIdProperties smileIdProperties;
    public static final String ID_APPROVED_STATUS = "1012";
    private static final Integer ENHANCED_JOB_TYPE = 5;

    List<String> errorCodes = List.of("1013", "1014");


    public BankAccountResponse resolveAccount(BankAccountRequest request) {

        AccountQueryResult result = paystackClient.getBalance(request.bankCode(), request.accountNumber());
        if (result.status()) {
            return new BankAccountResponse(AppUtil.titleCase(result.data().accountName()), null, null, null);
        }
        throw new BadRequestException("Unable to resolve account");
    }

    public List<BankCodeResponse> getBanks(ProviderCode providerCode) {
        return banksRepository.findBanksByProviderCode(providerCode.getValue());
    }


    @Override
    public BankAccountResponse bankDetailsQuery(BankDetailsQueryRequest request) {


        String timestamp = AppUtil.getSmileIdTimestamp();

        SmileIdEnhancedKycRequest.PartnerParams partnerParams = SmileIdEnhancedKycRequest.PartnerParams.builder()
                .job_id(UUID.randomUUID().toString())
                .job_type(ENHANCED_JOB_TYPE)
                .user_id(UUID.randomUUID().toString())
                .build();
        SmileIdEnhancedKycRequest smileIdEnhancedKycRequest = SmileIdEnhancedKycRequest.builder()
                .idNumber(request.idNumber())
                .idType(AppConstants.BANK_ACCOUNT)
                .country(request.country()).idType(AppConstants.BANK_ACCOUNT)
                .bankCode(request.bankCode())
                .partnerId(smileIdProperties.partnerId())
                .partnerParams(partnerParams)
                .timestamp(timestamp)
                .signature(getSignature(timestamp))
                .build();

        SmileIdWebhookNotification response = smileIdClient.enhancedBvnQuery(smileIdEnhancedKycRequest);
        if (ID_APPROVED_STATUS.equals(response.getResultCode()) && confirmSignature(response.getSignature(), response.getTimestamp())) {

            return new BankAccountResponse(
                    AppUtil.titleCase(response.getFullName()),
                    response.getFirstName(),
                    response.getLastName(),
                    response.getMiddleName()
            );

        } else if (errorCodes.contains(response.getResultCode())) {
            throw new ResourceNotFoundException("Enter a valid account number", AppConstants.BANK_ACCOUNT, request.idNumber());
        } else {
            throw new BadRequestException("Try again later.");
        }
    }

    @Override
    public UpdateResponse addAccount(AddAccountRequest request) {

        BankAccountResponse response = bankDetailsQuery(BankDetailsQueryRequest.builder()
                .idNumber(request.accountNumber())
                .bankCode(request.bankCode())
                .country(AppConstants.NG)
                .build());

        List<String> accountNames = new ArrayList<>();
        accountNames.add(response.firstName());
        accountNames.add(response.middleName());
        accountNames.add(response.lastName());
        List<String> personNames = new ArrayList<>();
        personNames.add(AppUtil.getFirstName());
        personNames.add(AppUtil.getLastName());
        personNames.add(AppUtil.getMiddleName());
        boolean match = AppUtil.allNamesMatch(personNames, accountNames);
        return UpdateResponse.builder().success(match).message(match ? "Account added" : "Enter a valid account").build();
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

    /**
     * Creates and initializes an HMAC-SHA256 {@link Mac} instance for signature generation.
     *
     * @param timestamp The timestamp to include in the MAC initialization.
     * @return The initialized {@link Mac} instance.
     * @throws NoSuchAlgorithmException If the HMAC-SHA256 algorithm is not available.
     * @throws InvalidKeyException      If the provided key is invalid.
     */
    private Mac getMac(String timestamp) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = AppUtil.getHmacSHA256();
        mac.init(new SecretKeySpec(smileIdProperties.apiKey().getBytes(), "HmacSHA256"));
        mac.update(timestamp.getBytes(StandardCharsets.UTF_8));
        mac.update(smileIdProperties.partnerId().getBytes(StandardCharsets.UTF_8));
        mac.update("sid_request".getBytes(StandardCharsets.UTF_8));
        return mac;
    }

    private String getSignature(String timestamp) {

        try {
            return Base64.getEncoder().encodeToString(getMac(timestamp).doFinal());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BadRequestException("Bad request: invalid request");
        }
    }
}
