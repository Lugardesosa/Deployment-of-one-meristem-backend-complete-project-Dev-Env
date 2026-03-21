package org.meristem.oneapp.usersservice.repositories;


import org.meristem.oneapp.usersservice.models.KycQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Transactional(readOnly = true)
public interface KycQueryRepository extends BaseRepository<KycQuery, Long> {
    KycQuery findKycQueryByJobIdAndUserId(String jobId, String userId);

    KycQuery findKycQueryByJobId(String jobId);

    KycQuery findKycQueryByJobIdAndStatus(String jobId, Integer status);

    Optional<KycQuery> findKycQueryByJobIdAndStatusIn(String jobId, Collection<Integer> statuses);
}
