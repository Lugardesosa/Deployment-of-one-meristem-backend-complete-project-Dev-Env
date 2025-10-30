package org.meristem.oneapp.trusteesservice.dtos.sql;

import org.meristem.oneapp.trusteesservice.domains.enums.Operator;

public record Comparison(Object value, Operator operator) {
}
