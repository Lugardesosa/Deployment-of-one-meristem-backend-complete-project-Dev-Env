package org.meristem.oneapp.usersservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.usersservice.domains.requests.AmlApprovalRequest;
import org.meristem.oneapp.usersservice.domains.requests.PastelAmlWebhookRequest;
import org.meristem.oneapp.usersservice.domains.responses.AmlApprovalResponse;
import org.meristem.oneapp.usersservice.domains.responses.AmlResponse;
import org.meristem.oneapp.usersservice.domains.responses.GetAmlResponse;
import org.meristem.oneapp.usersservice.domains.responses.WebhookResponse;
import org.meristem.oneapp.usersservice.integrations.responses.PastelAmlResponse;

public interface IAmlService {

    void performAmlRequest(KycCompletedDto kycCompletedDto);
    WebhookResponse handleAmlWebhook(PastelAmlWebhookRequest request);

    AmlResponse getAmlResults(Long userId) throws JsonProcessingException;

    AmlApprovalResponse approveAmlResults(@Valid AmlApprovalRequest request);

    GetAmlResponse getAmlApprovalResults(Long userId);
}
