package org.meristem.oneapp.trustiesservice.dtos.sql;

import org.meristem.oneapp.trustiesservice.domains.enums.Operator;

public record Comparison(Object value, Operator operator) {
}
