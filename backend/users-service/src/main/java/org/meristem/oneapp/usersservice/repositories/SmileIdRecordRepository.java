package org.meristem.oneapp.usersservice.repositories;


import org.meristem.oneapp.usersservice.models.SmileIdRecord;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface SmileIdRecordRepository extends BaseRepository<SmileIdRecord, Long> {
    SmileIdRecord findSmileIdRecordByJobIdAndUserId(String jobId, String userId);

    SmileIdRecord findSmileIdRecordByJobId(String jobId);
}
