package org.meristem.oneapp.trusteesservice.services;

import org.meristem.oneapp.trusteesservice.domains.enums.ActivityLogType;

import java.util.Map;

public interface IActivityLogService {

    <T> void sendActivity(Class<T> tClass, ActivityLogType activityLogType, Long entityId, Map<String, Object> metadata, String activity);
}
