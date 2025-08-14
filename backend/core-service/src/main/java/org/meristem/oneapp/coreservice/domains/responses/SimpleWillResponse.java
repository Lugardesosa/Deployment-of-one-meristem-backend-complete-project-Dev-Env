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
public class SimpleWillResponse extends PlanResponse {

    private String title;

    private String maritalStatus;
}
