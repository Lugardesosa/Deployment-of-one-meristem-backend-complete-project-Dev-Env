package org.meristem.oneapp.trusteesservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meristem.oneapp.trusteesservice.constants.AppConstants;

@AllArgsConstructor
@Getter
public enum GeneralFormType {

    ASSET(AppConstants.FORM_TYPE_ASSET),
    PLAN(AppConstants.FORM_TYPE_PLAN);

    private final String name;
}
