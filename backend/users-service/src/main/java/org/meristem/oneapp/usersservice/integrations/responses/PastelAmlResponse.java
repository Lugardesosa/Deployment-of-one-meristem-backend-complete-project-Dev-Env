package org.meristem.oneapp.usersservice.integrations.responses;

public record PastelAmlResponse(
        Data data
) {
    public record Data(
            String checkId
    ) {
    }
}
