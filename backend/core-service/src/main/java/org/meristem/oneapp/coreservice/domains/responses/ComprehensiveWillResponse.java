package org.meristem.oneapp.coreservice.domains.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class ComprehensiveWillResponse extends SimpleWillResponse {

    private String marriageType;

    private String religion;

    private String occupation;

    private String customaryTradition;

    private String traditionDetails;

    private String otherDetails;
}
