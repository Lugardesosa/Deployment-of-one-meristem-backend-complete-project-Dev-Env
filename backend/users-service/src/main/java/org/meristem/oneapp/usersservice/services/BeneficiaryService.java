package org.meristem.oneapp.usersservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.domains.requests.BeneficiaryRequest;
import org.meristem.oneapp.usersservice.domains.responses.BeneficiaryResponse;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.mappers.BeneficiaryMapper;
import org.meristem.oneapp.usersservice.models.Beneficiaries;
import org.meristem.oneapp.usersservice.repositories.CustomRepository;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeneficiaryService {

    private final CustomRepository customRepository;
    private final BeneficiaryMapper beneficiaryMapper = BeneficiaryMapper.INSTANCE;

    public BeneficiaryResponse createBeneficiary(BeneficiaryRequest request) {

        Long loggedInUserid = AppUtil.getLoggedInUserId();

        if (customRepository.existBy(Beneficiaries.class, Map.of("ownerId", loggedInUserid, "email", request.email()))) {
            throw new BadRequestException("Beneficiary with this email already exists");
        }

        Beneficiaries beneficiaries = beneficiaryMapper.beneficiaryRequestToBeneficiary(request);
        beneficiaries.setOwnerId(loggedInUserid);
        customRepository.save(beneficiaries);
        return beneficiaryMapper.beneficiaryToBeneficiaryResponse(beneficiaries);
    }

    public BeneficiaryResponse getBeneficiary(Long beneficiaryId, Long userId) {
        Map<String,Object> map = new HashMap<>();
        Long loggedInUserid = nonNull(userId) ? userId: AppUtil.getLoggedInUserId();
        map.put("ownerId", loggedInUserid);
        map.put("id", beneficiaryId);

        Optional<Beneficiaries> beneficiaries = customRepository.findOneBy(Beneficiaries.class, map);
        return beneficiaries.isPresent() ? beneficiaryMapper.beneficiaryToBeneficiaryResponse(beneficiaries.get()) : BeneficiaryResponse.builder().build();
    }

    public List<BeneficiaryResponse> getBeneficiaries(Long userId) {

        Long loggedInUserid = nonNull(userId) ? userId: AppUtil.getLoggedInUserId();
        return beneficiaryMapper.beneficiaryToBeneficiaryResponse(customRepository.findAllBy(Beneficiaries.class, Map.of("ownerId", loggedInUserid)));
    }
}
