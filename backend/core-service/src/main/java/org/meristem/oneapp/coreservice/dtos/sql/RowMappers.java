package org.meristem.oneapp.coreservice.dtos.sql;


import lombok.experimental.UtilityClass;
import org.meristem.oneapp.coreservice.domains.responses.BankResponse;
import org.meristem.oneapp.coreservice.domains.responses.FormResponse;
import org.springframework.jdbc.core.RowMapper;

@UtilityClass
public final class RowMappers {

    public static RowMapper<FormResponse.Selection> getSelectionValue() {
        return (rs, rowNum) -> FormResponse.Selection.builder().selectionValue(rs.getString("selection_value"))
                .additionalValue(rs.getString("additional_value")).build();
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
