package org.meristem.oneapp.trustiesservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "The different forms and their details", example =
        """
        {
            "name": "PUBLIC_EQUITIES",
            "position": 2,
            "displayName": "Public Equities"
        }
        """)
public record FormNamesResponse(String name, Integer position, String displayName) {
}
