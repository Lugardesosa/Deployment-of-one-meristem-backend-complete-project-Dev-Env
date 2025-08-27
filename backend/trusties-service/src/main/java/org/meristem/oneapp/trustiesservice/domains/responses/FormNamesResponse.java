package org.meristem.oneapp.trustiesservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
