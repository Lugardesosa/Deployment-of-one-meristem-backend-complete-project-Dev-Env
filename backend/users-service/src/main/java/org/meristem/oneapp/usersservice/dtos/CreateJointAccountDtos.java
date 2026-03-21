package org.meristem.oneapp.usersservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meristem.oneapp.usersservice.domains.enums.MandateType;
import org.meristem.oneapp.usersservice.domains.enums.OperationType;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateJointAccountDtos implements Serializable {
    private MandateType mandateType;
    private IdQueryDetailsDto primary;
    private IdQueryDetailsDto secondary;
}
