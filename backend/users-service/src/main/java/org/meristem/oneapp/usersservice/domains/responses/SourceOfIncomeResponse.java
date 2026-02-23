package org.meristem.oneapp.usersservice.domains.responses;

import java.util.List;

public record SourceOfIncomeResponse(List<SoI> sourceOfIncome) {

    public record SoI(Long id, String name) {

    }
}
