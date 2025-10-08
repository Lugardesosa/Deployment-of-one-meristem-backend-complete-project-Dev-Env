package org.meristem.oneapp.reportservice.dtos.sql;

import org.meristem.oneapp.reportservice.domains.enums.Operator;

public record Comparison(Object value, Operator operator) {
}
