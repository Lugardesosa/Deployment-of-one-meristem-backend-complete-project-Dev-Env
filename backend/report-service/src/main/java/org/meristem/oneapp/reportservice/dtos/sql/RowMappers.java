package org.meristem.oneapp.reportservice.dtos.sql;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.reportservice.domains.responses.ActivityLogResponse;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@UtilityClass
public class RowMappers {


    public ResultSetExtractor<List<ActivityLogResponse>> activityLogResponseExtractor() {
        return (rs) -> {
            Map<Long, ActivityLogResponse> activityLogs = new HashMap<>();
            Map<String, Object> metadata = new HashMap<>();
            while (rs.next()) {
                Long id = rs.getLong("id");

                ActivityLogResponse activityLogResponse = activityLogs.get(id);

                if (isNull(activityLogResponse)) {

                    String key = rs.getString("map_key");
                    String value = rs.getString("map_value");
                    if (nonNull(key) && nonNull(value)) {
                        metadata.put(key, value);
                    }

                    String action = rs.getString("action");
                    String actor = rs.getString("actor");
                    String entity = rs.getString("entity");
                    activityLogResponse = ActivityLogResponse.builder()
                            .action(action).id(rs.getLong("id"))
                            .activityDate(rs.getObject("activity_date", LocalDateTime.class))
                            .entity(entity)
                            .entityId(rs.getString("entity_id"))
                            .actor(actor).activity(rs.getString("activity"))
                            .application(rs.getString("application"))
                            .metadata(metadata)
                            .build();

                    activityLogs.put(id, activityLogResponse);
                } else {

                    String key = rs.getString("mapKey");
                    String value = rs.getString("mapValue");
                    if (nonNull(key) && nonNull(value)) {
                        activityLogResponse.metadata().put(key, value);
                    }

                }

            }
            return new ArrayList<>(activityLogs.values());
        };
    }
}
