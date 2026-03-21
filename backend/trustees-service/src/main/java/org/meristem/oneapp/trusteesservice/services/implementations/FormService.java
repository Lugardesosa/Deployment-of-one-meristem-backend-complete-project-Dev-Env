package org.meristem.oneapp.trusteesservice.services.implementations;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.trusteesservice.domains.enums.*;
import org.meristem.oneapp.trusteesservice.domains.responses.BankResponse;
import org.meristem.oneapp.trusteesservice.domains.responses.FormNamesResponse;
import org.meristem.oneapp.trusteesservice.domains.responses.FormResponse;
import org.meristem.oneapp.trusteesservice.dtos.sql.RowMappers;
import org.meristem.oneapp.trusteesservice.integrations.UserServiceClient;
import org.meristem.oneapp.trusteesservice.models.Banks;
import org.meristem.oneapp.trusteesservice.models.Currencies;
import org.meristem.oneapp.trusteesservice.models.Selections;
import org.meristem.oneapp.trusteesservice.repositories.CustomRepository;
import org.meristem.oneapp.trusteesservice.repositories.FormRepository;
import org.meristem.oneapp.trusteesservice.services.IFormService;
import org.meristem.oneapp.trusteesservice.utils.AppUtil;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.meristem.oneapp.trusteesservice.dtos.sql.RowMappers.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormService implements IFormService {

    public static final String BENEFICIARY = "Beneficiary";
    public static final String ASSET_CATEGORY = "Asset Category";
    private final CustomRepository customRepository;
    private final FormRepository formRepository;
    private final UserServiceClient userServiceClient;

    public FormResponse getForm(FormName form) {

        Long userId = AppUtil.getLoggedInUserId();
        Map<String, Object> selections = new HashMap<>();
        List<FormResponse.FormData> formResponses = formRepository.findFormsByInternalOrder(form.getInternalOrder());
        List<FormResponse.Currency> currencies = customRepository.findAll(Currencies.class, Map.of(), getCurrencyRowMapper());

        for (FormResponse.FormData formData : formResponses) {
            if (formData.getType().equals(FormType.SELECTION.getValue())) {

                switch (formData.getLabel()) {
                    case BENEFICIARY ->
                            selections.put(formData.getLabel(), userServiceClient.getAllBeneficiaries(userId).data());
                    case ASSET_CATEGORY ->
                            selections.put(formData.getLabel(), Arrays.stream(Assets.values()).map(Assets::getDisplayName).toList());
                    case "EMPTY" -> {
                        List<Object> values = new ArrayList<>();
                        Arrays.stream(Assets.values()).forEach(asset -> {
                            Map<String, Object> assetValues = new HashMap<>();
                            assetValues.put("label", asset.getLabel());
                            assetValues.put("placeholder", asset.getPlaceholder());
                            assetValues.put(asset.getName(), customRepository.findAll(asset.getClazz(), Map.of("owner_id", userId, "status", EntityStatus.ACTIVE.getValue()), asset.getRowMapper()));
                            values.add(assetValues);
                        });
                        selections.put("assets", values);
                    }
                    case null, default ->
                            selections.put(formData.getLabel(), customRepository.findAll(Selections.class, Map.of("form_id", formData.getId()), getSelectionValue()));
                }
            }

            if (formData.getType().equals(FormType.RADIO.getValue())) {
                selections.put(formData.getLabel(), customRepository.findAll(Selections.class, Map.of("form_id", formData.getId()), getRadioValue()));
            }
        }

        return FormResponse.builder().formData(formResponses).currencies(currencies).selections(selections).build();
    }

    public List<FormNamesResponse> getFormNames(GeneralFormType generalFormType) {

        List<FormName> skip = List.of(FormName.DIGITAL_PLATFORM, FormName.CRYPTOCURRENCY_AND_NFT, FormName.FINTECH_WALLET);

        return Arrays.stream(FormName.values()).filter(f -> f.getType().equals(generalFormType.getName()))
                .filter(f -> !skip.contains(f))
                .map(fn -> {

                    FormNamesResponse build = FormNamesResponse.builder().displayName(fn.getDisplayName())
                            .position(fn.getPosition()).name(fn.name()).build();

                    if (FormName.ALTERNATE_ASSETS.equals(fn)) {
                        build.setSubs(skip.stream().map(fc -> FormNamesResponse.builder().displayName(fc.getDisplayName())
                                .position(fc.getPosition()).name(fc.name()).build()).toList());
                    }

                    return build;

                })
                .toList();
    }

    public List<BankResponse> getBankNames() {
        return customRepository.findAll(Banks.class, Map.of(), RowMappers.getBankNames());
    }
}
