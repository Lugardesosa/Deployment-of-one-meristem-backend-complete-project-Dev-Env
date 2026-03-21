package org.meristem.oneapp.reportservice.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.kafka.dtos.ActivityLogEventDto;
import org.meristem.oneapp.reportservice.models.ActivityLog;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ActivityLogMapper {

    ActivityLogMapper INSTANCE = Mappers.getMapper(ActivityLogMapper.class);

    ActivityLog activityLogEventDtoToActivityLog(ActivityLogEventDto activityLogEventDto);
}
