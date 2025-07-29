package org.meristem.oneapp.coreservice.dtos.sql;

import org.meristem.oneapp.coreservice.domains.enums.Operator;

public record Comparison(Object value, Operator operator) {
}
