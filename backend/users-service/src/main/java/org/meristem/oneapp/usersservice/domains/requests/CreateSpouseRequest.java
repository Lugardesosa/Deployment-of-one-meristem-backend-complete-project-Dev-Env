package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.usersservice.domains.enums.MaritalStatus;
import org.meristem.oneapp.usersservice.domains.enums.UserTitles;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public record CreateSpouseRequest(@NotNull(message = "Cannot be null") MaritalStatus maritalStatus, UserTitles title, String fullName, String email, Long nationalityId, String phoneNumber, String phoneNumberFormat) {

    public boolean validateData() {
        return MaritalStatus.MARRIED.compareTo(maritalStatus) != 0 || (nonNull(title()) && isNotBlank(fullName()) && isNotBlank(email()) && nonNull(nationalityId()) && isNotBlank(phoneNumber()) && isNotBlank(phoneNumberFormat()));
    }

    @Override
    public String phoneNumberFormat() {
        return phoneNumberFormat.replace("+", "");
    }
}
