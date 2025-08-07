package org.meristem.oneapp.coreservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meristem.oneapp.coreservice.constants.AppConstants;

@AllArgsConstructor
@Getter
public enum GeneralFormType {

    ASSET(AppConstants.FORM_TYPE_ASSET),
    PLAN(AppConstants.FORM_TYPE_PLAN);

    private final String name;
}
