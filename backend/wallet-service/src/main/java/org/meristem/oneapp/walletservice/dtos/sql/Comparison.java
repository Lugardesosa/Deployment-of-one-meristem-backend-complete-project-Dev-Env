package org.meristem.oneapp.walletservice.dtos.sql;

import org.meristem.oneapp.walletservice.domains.enums.Operator;

public record Comparison(Object value, Operator operator) {
}
