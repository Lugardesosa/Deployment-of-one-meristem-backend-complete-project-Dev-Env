package org.meristem.oneapp.usersservice.dtos.sql;

import org.meristem.oneapp.usersservice.domains.enums.Operator;

public record Comparison(Object value, org.meristem.oneapp.usersservice.domains.enums.Operator operator) {
}
