package org.meristem.oneapp.usersservice.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.usersservice.domains.enums.NextOfKins;
import org.meristem.oneapp.usersservice.domains.requests.CreateNextOfKinRequest;
import org.meristem.oneapp.usersservice.domains.responses.NextOfKinResponse;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.mappers.NextOfKinMapping;
import org.meristem.oneapp.usersservice.models.NextOfKin;
import org.meristem.oneapp.usersservice.repositories.NextOfKinRepository;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
@Slf4j
public class NextOfKinService {

    private final NextOfKinRepository nextOfKinRepository;
    private final NextOfKinMapping nextOfKinMapping = Mappers.getMapper(NextOfKinMapping.class);

    @Transactional
    public NextOfKinResponse createNextOfKin(CreateNextOfKinRequest createNextOfKinRequest) {

        NextOfKins nextOfKins = NextOfKins.fromName(createNextOfKinRequest.nextOfKins());
        if (nextOfKins.equals(NextOfKins.OTHERS) && !StringUtils.hasText(createNextOfKinRequest.relationship())) {
            throw new BadRequestException("Relationship is required if OTHERS is passed");
        }

        if (createNextOfKinRequest.email().equals(AppUtil.getLoggedInUserEmail())) {
            throw new BadRequestException("You cannot use your email as your next of kin's email");
        }

        if (createNextOfKinRequest.phoneNumber().equals(AppUtil.getLoggedInUserPhone())) {
            throw new BadRequestException("You cannot use your phone number as your next of kin's phone number");
        }

        if (nextOfKinRepository.existsByUserId(AppUtil.getLoggedInUserId())) {
            throw new BadRequestException("Contact us to update your next-of-kin");
        }

        NextOfKin nextOfKin = nextOfKinMapping.createNextOfKinRequestToNextOfKin(createNextOfKinRequest);
        nextOfKin.setRelationship(nextOfKins.getDisplayName());
        nextOfKin.setUserId(AppUtil.getLoggedInUserId());
        nextOfKinRepository.save(nextOfKin);
        return nextOfKinMapping.NextOfKinToCreateNextOfKindResponse(nextOfKin);
    }

    // TODO: CREATE AN UPDATE ENDPOINT THAT CAN ONLY BE USED BY ADMINS
    // updateNextOfKin
}