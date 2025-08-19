package org.meristem.oneapp.trustiesservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meristem.oneapp.trustiesservice.dtos.sql.RowMappers;
import org.meristem.oneapp.trustiesservice.models.ComprehensiveWill;
import org.meristem.oneapp.trustiesservice.models.NominatedFund;
import org.meristem.oneapp.trustiesservice.models.SimpleWill;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.List;

@AllArgsConstructor
@Getter
public enum Plans {

    SIMPLE_WILL("Simple Will", 14, RowMappers.getSimpleWill(), SimpleWill.class),
    COMPREHENSIVE_WILL("Comprehensive Will", 15, RowMappers.getComprehensiveWill(), ComprehensiveWill.class),
//    EDUCATION_TRUST("Education Trust", 16),
//    LIVING_TRUST("Living Trust", 17),
    NOMINATED_FUND("Nominated Fund", 18, RowMappers.getNominatedFund(), NominatedFund.class);
//    TESTAMENTARY_TRUST("Testamentary Trust", 19);

    private final String name;
    private final int value;
    private final ResultSetExtractor<List<?>> rowMapper;
    private final Class<?> clazz;
}
