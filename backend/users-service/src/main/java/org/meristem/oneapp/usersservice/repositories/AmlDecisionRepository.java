package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.domains.responses.GetAmlResponse;
import org.meristem.oneapp.usersservice.models.AmlDecision;

import java.util.List;

public interface AmlDecisionRepository extends BaseRepository<AmlDecision, Long> {
    List<GetAmlResponse.GetAmlApprovalResponse> findAmlDecisionsByEntityId(Long entityId);
}
