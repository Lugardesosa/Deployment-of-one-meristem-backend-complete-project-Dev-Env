package org.meristem.oneapp.trusteesservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema(description = "The different forms and their details", example =
        """
                        {
                            "name": "PUBLIC_EQUITIES",
                            "position": 2,
                            "displayName": "Public Equities",
                            "subs": [
                                    {
                            "name": "PUBLIC_EQUITIES",
                            "position": 2,
                            "displayName": "Public Equities"
                        }
                            ]
                        }
                """)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormNamesResponse {
    private String name;
    private Integer position;
    private String displayName;
    private List<FormNamesResponse> subs;
}
