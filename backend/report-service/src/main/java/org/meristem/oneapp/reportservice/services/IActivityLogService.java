package org.meristem.oneapp.reportservice.services;


import org.meristem.oneapp.kafka.dtos.ActivityLogEventDto;
import org.meristem.oneapp.reportservice.domains.requests.ActivityLogRequest;
import org.meristem.oneapp.reportservice.domains.responses.ActivityLogResponse;
import org.springframework.data.domain.Page;

public interface IActivityLogService {

    void saveActivityLogs(ActivityLogEventDto value);

    Page<ActivityLogResponse> getActivityLogs(ActivityLogRequest request);
}
