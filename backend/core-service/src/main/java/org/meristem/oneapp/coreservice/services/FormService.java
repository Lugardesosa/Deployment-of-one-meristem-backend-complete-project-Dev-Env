package org.meristem.oneapp.coreservice.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.coreservice.domains.enums.FormType;
import org.meristem.oneapp.coreservice.domains.responses.BankResponse;
import org.meristem.oneapp.coreservice.domains.responses.FormNamesResponse;
import org.meristem.oneapp.coreservice.domains.responses.FormResponse;
import org.meristem.oneapp.coreservice.domains.enums.FormName;
import org.meristem.oneapp.coreservice.dtos.sql.RowMappers;
import org.meristem.oneapp.coreservice.models.Banks;
import org.meristem.oneapp.coreservice.models.Currencies;
import org.meristem.oneapp.coreservice.models.Selections;
import org.meristem.oneapp.coreservice.repositories.CustomRepository;
import org.meristem.oneapp.coreservice.repositories.FormRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;
import static org.meristem.oneapp.coreservice.dtos.sql.RowMappers.getCurrencyRowMapper;
import static org.meristem.oneapp.coreservice.dtos.sql.RowMappers.getSelectionValue;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormService {

    private final CustomRepository customRepository;
    private final FormRepository formRepository;

    public FormResponse getForm(FormName form) {

        Map<Integer, Object> selections = new HashMap<>();
        List<FormResponse.FormData> formResponses = formRepository.findFormsByFormPosition(form.getPosition());
        List<FormResponse.Currency> currencies = customRepository.findAll(Currencies.class, Map.of(), getCurrencyRowMapper());

        for (FormResponse.FormData formData : formResponses) {
            if (formData.getType().equals(FormType.SELECTION.getValue())) {
                selections.put(formData.getFieldOrder(), customRepository.findAll(Selections.class, Map.of("form_id", formData.getId()), getSelectionValue()));
            }
        }

        return FormResponse.builder().formData(formResponses).currencies(currencies).selections(selections).build();
    }

    public List<FormNamesResponse> getFormNames() {
        return Arrays.stream(FormName.values()).map(fn -> FormNamesResponse.builder().displayName(fn.getDisplayName())
                .position(fn.getPosition()).name(fn.name()).build())
                .toList();
    }

    public List<BankResponse> getBankNames() {
        return customRepository.findAll(Banks.class, Map.of(), RowMappers.getBankNames());
    }
}
