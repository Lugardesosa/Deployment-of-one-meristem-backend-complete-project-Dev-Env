package org.meristem.oneapp.usersservice.integrations.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record PastelAmlResponse(String message,
                                int count,
                                int pageNumber,
                                List<EntityData> data) {

    public record EntityData(

            @JsonProperty("_id")
            String id,

            String type,

            String externalId,

            @JsonProperty("__v")
            int version,

            List<String> addresses,
            List<String> aliases,

            @JsonProperty("birth_date")
            LocalDate birthDate,

            List<String> countries,

            Instant createdAt,
            Instant updatedAt,

            List<Dataset> dataset,

            String entityType,
            String name,

            List<String> sanctions,

            String photo,

            List<String> politicalParty,

            String gender,

            List<Position> positions,
            List<Education> education,

            List<String> tags,

            int searchScore,
            int confidenceScore

    ) {

        public record Dataset(
                String name,
                String url
        ) {
        }

        public record Position(
                String title,
                String startDate,
                String endDate,

                @JsonProperty("_id")
                String id
        ) {
        }

        public record Education(
                String title,
                String startDate,
                String endDate,

                @JsonProperty("_id")
                String id
        ) {
        }
    }
}
