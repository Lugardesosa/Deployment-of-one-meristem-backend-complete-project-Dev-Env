package org.meristem.oneapp.usersservice.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.usersservice.config.configProperties.PastelProperties;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.enums.AmlEntityType;
import org.meristem.oneapp.usersservice.domains.enums.AmlResultStatus;
import org.meristem.oneapp.usersservice.domains.enums.AmlResultType;
import org.meristem.oneapp.usersservice.domains.requests.AmlApprovalRequest;
import org.meristem.oneapp.usersservice.domains.requests.PastelAmlWebhookRequest;
import org.meristem.oneapp.usersservice.domains.responses.AmlApprovalResponse;
import org.meristem.oneapp.usersservice.domains.responses.AmlResponse;
import org.meristem.oneapp.usersservice.domains.responses.GetAmlResponse;
import org.meristem.oneapp.usersservice.domains.responses.WebhookResponse;
import org.meristem.oneapp.usersservice.dtos.AdverseMediaDto;
import org.meristem.oneapp.usersservice.dtos.PepDto;
import org.meristem.oneapp.usersservice.dtos.SanctionDto;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.integrations.PastelClient;
import org.meristem.oneapp.usersservice.integrations.requests.PastelAmlRequest;
import org.meristem.oneapp.usersservice.integrations.responses.PastelAmlResponse;
import org.meristem.oneapp.usersservice.mappers.AmlMapper;
import org.meristem.oneapp.usersservice.models.AmlDecision;
import org.meristem.oneapp.usersservice.models.AmlResult;
import org.meristem.oneapp.usersservice.models.AmlSearch;
import org.meristem.oneapp.usersservice.models.Vendor;
import org.meristem.oneapp.usersservice.repositories.AmlDecisionRepository;
import org.meristem.oneapp.usersservice.repositories.AmlResultRepository;
import org.meristem.oneapp.usersservice.repositories.AmlSearchRepository;
import org.meristem.oneapp.usersservice.repositories.AmlVendorRepository;
import org.meristem.oneapp.usersservice.services.IAmlService;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class AmlService implements IAmlService {

    private final PastelClient pastelClient;
    private final PastelProperties pastelProperties;
    private final AmlResultRepository amlResultRepository;
    private final AmlVendorRepository amlVendorRepository;
    private final AmlSearchRepository amlSearchRepository;
    private final ObjectMapper objectMapper;
    private final AmlMapper amlMapper = AmlMapper.INSTANCE;
    private final AmlDecisionRepository amlDecisionRepository;

    @Override
    public void performAmlRequest(KycCompletedDto kycCompletedDto) {

        String fullname = kycCompletedDto.firstName() + " " + kycCompletedDto.lastName();
        PastelAmlRequest request = PastelAmlRequest.builder().name(fullname).threshold(pastelProperties.threshold())
                .limit(pastelProperties.limit()).callbackUrl(pastelProperties.callbackUrl()).build();
        PastelAmlResponse response = pastelClient.amlRiskMonitoring(request);
        amlSearchRepository.save(AmlSearch.builder().clientSearchId(response.data().checkId()).entityId(kycCompletedDto.userId()).build());
        log.info("Pastel request successfully completed {}", response);
    }

    @Transactional
    @Override
    public WebhookResponse handleAmlWebhook(PastelAmlWebhookRequest request) {
        try {

            Vendor vendor = amlVendorRepository.findAmlVendorByVendorCode(AppConstants.VENDOR_PASTEL);
            AmlSearch amlSearch = amlSearchRepository.findAmlSearchByClientSearchId(request.checkId());

            List<AmlResult> results = new ArrayList<>();
            for (PastelAmlWebhookRequest.AdverseMediaDate.AdverseResult adverseResult : request.adverseMedia().result()) {

                AdverseMediaDto adverseMediaDto = amlMapper.adverseResultToAdverseDto(adverseResult);

                AmlResult result = AmlResult.builder()
                        .vendorId(vendor.getId())
                        .entityType(AmlEntityType.USER.getName())
                        .entityId(amlSearch.getEntityId())
                        .vendorReference(request.checkId())
                        .searchId(amlSearch.getId())
                        .vendorDataset(objectMapper.valueToTree(adverseMediaDto))
                        .resultType(AmlResultType.ADVERSE_MEDIA.getName())
                        .build();
                result.setStatus(AmlResultStatus.UNUSED.getValue());
                results.add(result);

            }
            amlResultRepository.saveAll(results);

            List<AmlResult> pepDtoResults = new ArrayList<>();
            for (PastelAmlWebhookRequest.EntityDataPep.PepData pepResult : request.pep().results()) {

                PepDto pepDto = PepDto.builder()
                        .aliases(pepResult.aliases())
                        .addresses(pepResult.addresses())
                        .birthDate(List.of(pepResult.birthDate().split(";")))
                        .countries(pepResult.countries())
                        .name(pepResult.name())
                        .photo(pepResult.photo())
                        .gender(pepResult.gender())
                        .confidenceScore(pepResult.confidenceScore())
                        .checkCreationDate(request.pep().checkCreationDate())
                        .lastScreenedDate(request.pep().lastScreenedDate())
                        .deceased(pepResult.deceased())
                        .build();
                AmlResult result = AmlResult.builder()
                        .vendorId(vendor.getId())
                        .entityType(AmlEntityType.USER.getName())
                        .entityId(amlSearch.getEntityId())
                        .vendorReference(request.checkId())
                        .searchId(amlSearch.getId())
                        .vendorDataset(objectMapper.valueToTree(pepDto))
                        .resultType(AmlResultType.PEP.getName())
                        .build();
                result.setStatus(AmlResultStatus.UNUSED.getValue());
                pepDtoResults.add(result);
            }
            amlResultRepository.saveAll(pepDtoResults);


            List<AmlResult> sanctionDtoList = new ArrayList<>();
            for (PastelAmlWebhookRequest.EntityDataSanction.SanctionData sanctionResult : request.sanction().results()) {

                var year = List.of(sanctionResult.birthDate().split(";"));
                SanctionDto sanctionDto = SanctionDto.builder()
                        .aliases(sanctionResult.aliases())
                        .addresses(sanctionResult.addresses())
                        .birthDate(List.of(sanctionResult.birthDate().split(";")))
                        .countries(sanctionResult.countries())
                        .name(sanctionResult.name())
                        .sanctions(sanctionResult.sanctions())
                        .confidenceScore(sanctionResult.confidenceScore())
                        .checkCreationDate(request.sanction().checkCreationDate())
                        .lastScreenedDate(request.sanction().lastScreenedDate())
                        .deceased(sanctionResult.deceased())
                        .build();

                AmlResult result = AmlResult.builder()
                        .vendorId(vendor.getId())
                        .entityType(AmlEntityType.USER.getName())
                        .entityId(amlSearch.getEntityId())
                        .vendorReference(request.checkId())
                        .searchId(amlSearch.getId())
                        .vendorDataset(objectMapper.valueToTree(sanctionDto))
                        .resultType(AmlResultType.SANCTION.getName())
                        .build();
                result.setStatus(AmlResultStatus.UNUSED.getValue());
                sanctionDtoList.add(result);
            }
            amlResultRepository.saveAll(sanctionDtoList);

            return WebhookResponse.builder().message("Successful").success(true).build();
        } catch (RuntimeException e) {
            throw new BadRequestException("Request could not be completed.");
        }
    }

    @Override
    public AmlResponse getAmlResults(Long userId) {

        List<AmlResult> results = amlResultRepository.findAmlResultsByEntityIdAndEntityTypeAndStatus(userId, AmlEntityType.USER.getName(), AmlResultStatus.UNUSED.getValue());

        List<AdverseMediaDto> adverseMediaDtos = new ArrayList<>();
        List<PepDto> pepDtos = new ArrayList<>();
        List<SanctionDto> sanctionDtos = new ArrayList<>();

        for (AmlResult result : results) {
            if (AmlResultType.PEP.getName().equalsIgnoreCase(result.getResultType())) {
                try {
                    pepDtos.add(objectMapper.treeToValue(result.getVendorDataset(), PepDto.class));
                } catch (JsonProcessingException ex) {
                    log.error("Error converting value pep -- {}", ex.getMessage());
                }
            } else if (AmlResultType.ADVERSE_MEDIA.getName().equalsIgnoreCase(result.getResultType())) {
                try {
                    adverseMediaDtos.add(objectMapper.treeToValue(result.getVendorDataset(), AdverseMediaDto.class));
                } catch (JsonProcessingException ex) {
                    log.error("Error converting value adverse -- {}", ex.getMessage());
                }
            } else if (AmlResultType.SANCTION.getName().equalsIgnoreCase(result.getResultType())) {
                try {
                    sanctionDtos.add(objectMapper.treeToValue(result.getVendorDataset(), SanctionDto.class));
                } catch (JsonProcessingException ex) {
                    log.error("Error converting value sanction -- {}", ex.getMessage());
                }
            }
        }
        return AmlResponse.builder().userId(userId).pepDtos(pepDtos).adverseMediaDtos(adverseMediaDtos).sanctionDtos(sanctionDtos).build();
    }

    @Transactional
    @Override
    public AmlApprovalResponse approveAmlResults(AmlApprovalRequest request) {

        List<AmlResult> results = amlResultRepository.findAmlResultsByEntityIdAndSearchIdAndStatus(request.userId(), request.searchId(), AmlResultStatus.UNUSED.getValue());
        if (results.isEmpty()) {
            throw new BadRequestException("Aml details not found");
        }
        ;
        String decisionGroupId = UUID.randomUUID().toString();

        AmlDecision pep = AmlDecision.builder().decision(request.pep().amlDecision().getName())
                .decidedBy(AppUtil.getLoggedInUserEmail())
                .entityId(request.userId())
                .amlType(AmlResultType.PEP.getName())
                .decisionGroupId(decisionGroupId)
                .decidedDate(LocalDateTime.now())
                .reason(request.pep().reason()).build();

        AmlDecision sanction = AmlDecision.builder().decision(request.sanction().amlDecision().getName())
                .decidedBy(AppUtil.getLoggedInUserEmail())
                .entityId(request.userId())
                .amlType(AmlResultType.SANCTION.getName())
                .decisionGroupId(decisionGroupId)
                .decidedDate(LocalDateTime.now())
                .reason(request.sanction().reason()).build();

        AmlDecision adverseMedia = AmlDecision.builder().decision(request.adverseMedia().amlDecision().getName())
                .decidedBy(AppUtil.getLoggedInUserEmail())
                .entityId(request.userId())
                .amlType(AmlResultType.ADVERSE_MEDIA.getName())
                .decisionGroupId(decisionGroupId)
                .decidedDate(LocalDateTime.now())
                .reason(request.adverseMedia().reason()).build();

        amlDecisionRepository.saveAll(List.of(pep, sanction, adverseMedia));
        results.forEach(r -> r.setStatus(AmlResultStatus.USED.getValue()));
        amlResultRepository.saveAll(results);
        return AmlApprovalResponse.builder().status(true).message("Successful").build();
    }

    @Override
    public GetAmlResponse getAmlApprovalResults(Long userId) {

        List<GetAmlResponse.GetAmlApprovalResponse> approvalResponseList = amlDecisionRepository.findAmlDecisionsByEntityId(userId);

        return new GetAmlResponse(userId, approvalResponseList);
    }
}
