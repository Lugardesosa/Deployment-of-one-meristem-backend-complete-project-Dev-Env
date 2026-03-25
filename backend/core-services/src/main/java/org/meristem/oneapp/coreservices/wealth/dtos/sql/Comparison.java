package org.meristem.oneapp.coreservices.wealth.dtos.sql;

import org.meristem.oneapp.coreservices.wealth.domains.enums.Operator;

public record Comparison(Object value, Operator operator) {
}
