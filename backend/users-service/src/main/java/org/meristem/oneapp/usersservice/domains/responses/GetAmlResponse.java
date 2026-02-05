package org.meristem.oneapp.usersservice.domains.responses;

import java.time.LocalDateTime;
import java.util.List;

public record GetAmlResponse(Long userId, List<GetAmlApprovalResponse> approvalResponseList) {
    public record GetAmlApprovalResponse(Long id, String decisionGroupId, String decision, String amlType, String decidedBy, LocalDateTime decidedDate, String reason) {

    }

}
