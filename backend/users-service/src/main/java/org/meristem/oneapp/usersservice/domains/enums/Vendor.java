package org.meristem.oneapp.usersservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.kafka.common.protocol.types.Field;

@AllArgsConstructor
@Getter
public enum Vendor {

    SMILE_ID("SMILE_ID"), DOJAH("DOJAH"), PASTEL("PASTEL");

    private final String value;
}
