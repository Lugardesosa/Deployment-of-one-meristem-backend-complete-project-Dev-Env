package org.meristem.oneapp.trustiesservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meristem.oneapp.trustiesservice.dtos.sql.RowMappers;
import org.meristem.oneapp.trustiesservice.models.ComprehensiveWill;
import org.meristem.oneapp.trustiesservice.models.NominatedFund;
import org.meristem.oneapp.trustiesservice.models.PrivateTrusts;
import org.meristem.oneapp.trustiesservice.models.SimpleWill;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.List;

@AllArgsConstructor
@Getter
public enum Plans {

    SIMPLE_WILL("Simple Will", 1, RowMappers.getSimpleWill(), SimpleWill.class, true, 14),
    COMPREHENSIVE_WILL("Comprehensive Will", 2, RowMappers.getComprehensiveWill(), ComprehensiveWill.class, true, 15),
    PRIVATE_TRUSTS("Private Trusts", 3, RowMappers.getPrivateTrusts(), PrivateTrusts.class, false, 17),
    NOMINATED_FUND("Nominated Fund", 4, RowMappers.getNominatedFund(), NominatedFund.class, false, 16);

    private final String name;
    private final int value;
    private final ResultSetExtractor<List<?>> rowMapper;
    private final Class<?> clazz;
    private final boolean withAssets;
    private final int internalOrder;
}
