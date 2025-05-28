package org.meristem.oneapp.usersservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.usersservice.domains.requests.CreateNextOfKinRequest;
import org.meristem.oneapp.usersservice.domains.responses.NextOfKinResponse;
import org.meristem.oneapp.usersservice.models.NextOfKin;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NextOfKinMapping {

    NextOfKinMapping INSTANCE = Mappers.getMapper(NextOfKinMapping.class);

    NextOfKin createNextOfKinRequestToNextOfKin(CreateNextOfKinRequest nextOfKinRequest);
    NextOfKinResponse NextOfKinToCreateNextOfKindResponse(NextOfKin nextOfKin);

    NextOfKinResponse nextOfKinToNextOfKinResponse(NextOfKin nextOfKin);
}
