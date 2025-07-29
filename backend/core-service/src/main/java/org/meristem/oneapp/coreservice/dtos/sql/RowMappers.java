package org.meristem.oneapp.coreservice.dtos.sql;


import lombok.experimental.UtilityClass;
import org.meristem.oneapp.coreservice.domains.responses.BankResponse;
import org.meristem.oneapp.coreservice.domains.responses.FormResponse;
import org.springframework.jdbc.core.RowMapper;

@UtilityClass
public final class RowMappers {

    public static RowMapper<String> getSelectionValue() {
        return (rs, rowNum) -> rs.getString("selection_value");
    }

    public static RowMapper<BankResponse> getBankNames() {
        return (rs, rowNum) ->
                BankResponse.builder()
                        .name(rs.getString("name")).id(rs.getLong("id"))
                        .build();
    }

    public static RowMapper<FormResponse.Currency> getCurrencyRowMapper() {
        return (rs, rn) ->
                FormResponse.Currency.builder()
                        .currencyName(rs.getString("currency_name"))
                        .currencyLogo("currency_logo").build();
    }
}
