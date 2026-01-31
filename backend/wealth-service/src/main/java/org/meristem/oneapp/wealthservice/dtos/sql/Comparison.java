package org.meristem.oneapp.wealthservice.dtos.sql;

import org.meristem.oneapp.wealthservice.domains.enums.Operator;

public record Comparison(Object value, Operator operator) {
}
