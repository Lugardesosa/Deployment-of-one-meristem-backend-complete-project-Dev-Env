package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.AmlResult;

import java.util.List;

public interface AmlResultRepository extends BaseRepository<AmlResult, Long> {
    List<AmlResult> findAmlResultsByEntityIdAndEntityType(Long entityId, String entityType);

    boolean existsAmlResultsByEntityIdAndSearchId(Long entityId, Long searchId);

    List<AmlResult> findAmlResultsByEntityIdAndEntityTypeAndStatus(Long entityId, String entityType, Integer status);

    List<AmlResult> findAmlResultsByEntityIdAndSearchIdAndStatus(Long entityId, Long searchId, Integer status);
}
