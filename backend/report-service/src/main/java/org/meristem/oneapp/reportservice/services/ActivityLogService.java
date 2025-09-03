package org.meristem.oneapp.reportservice.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.ActivityLogEventDto;
import org.meristem.oneapp.reportservice.domains.requests.ActivityLogRequest;
import org.meristem.oneapp.reportservice.domains.responses.ActivityLogResponse;
import org.meristem.oneapp.reportservice.dtos.sql.RowMappers;
import org.meristem.oneapp.reportservice.mappers.ActivityLogMapper;
import org.meristem.oneapp.reportservice.models.ActivityLog;
import org.meristem.oneapp.reportservice.models.ActivityLogMetadata;
import org.meristem.oneapp.reportservice.repositories.CustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ActivityLogService {

    private final CustomRepository customRepository;
    private final ActivityLogMapper activityLogMapper = ActivityLogMapper.INSTANCE;

    public void saveActivityLogs(ActivityLogEventDto value) {

        ActivityLog activityLog = activityLogMapper.activityLogEventDtoToActivityLog(value);
        customRepository.save(activityLog);

        customRepository.saveAll(value.metadata().entrySet().stream().map(entry ->
                ActivityLogMetadata.builder().activityLogId(activityLog.getId()).mapKey(entry.getKey())
                        .mapValue(String.valueOf(entry.getValue())).build()).toList());

    }


    public Page<ActivityLogResponse> getActivityLogs(ActivityLogRequest request) {

        Map<String, Object> filters = new HashMap<>();

        if (nonNull(request.getAction())) {
            filters.put("action", request.getAction().name());
        }
        if (nonNull(request.getActivityId())) {
            filters.put("activity_id", request.getActivityId());
        }
        if (nonNull(request.getApplication())) {
            filters.put("application", request.getApplication());
        }
        if (nonNull(request.getActor())) {
            filters.put("actor", request.getActor());
        }
        if (nonNull(request.getEntity())) {
            filters.put("entity", request.getEntity());
        }

        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize(), Sort.by(request.getSortOrder(), String.join(",", request.getSortBy())));

        List<ActivityLogResponse> page = customRepository.findAllActivityLogs(filters, RowMappers.activityLogResponseExtractor(), pageRequest);

        return new PageImpl<>(page, pageRequest, page.size());

    }
}
