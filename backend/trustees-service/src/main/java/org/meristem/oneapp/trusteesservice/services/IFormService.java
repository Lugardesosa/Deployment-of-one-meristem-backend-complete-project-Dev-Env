package org.meristem.oneapp.trusteesservice.services;

import org.meristem.oneapp.trusteesservice.domains.enums.FormName;
import org.meristem.oneapp.trusteesservice.domains.enums.GeneralFormType;
import org.meristem.oneapp.trusteesservice.domains.responses.BankResponse;
import org.meristem.oneapp.trusteesservice.domains.responses.FormNamesResponse;
import org.meristem.oneapp.trusteesservice.domains.responses.FormResponse;

import java.util.List;

public interface IFormService {

    FormResponse getForm(FormName form);

    List<FormNamesResponse> getFormNames(GeneralFormType generalFormType);

    List<BankResponse> getBankNames();
}
