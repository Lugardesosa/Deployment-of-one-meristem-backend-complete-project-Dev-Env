package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AmlResultType {

    PEP("PEP"),
    SANCTION("SANCTION"),
    ADVERSE_MEDIA("ADVERSE_MEDIA");


    private final String name;
}
