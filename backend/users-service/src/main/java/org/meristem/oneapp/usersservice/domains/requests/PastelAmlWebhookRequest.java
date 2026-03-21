package org.meristem.oneapp.usersservice.domains.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public record PastelAmlWebhookRequest(
        String checkId,
        EntityDataPep pep,
        EntityDataSanction sanction,
        AdverseMediaDate adverseMedia
) {


    public record EntityDataPep(
            @JsonProperty("check_id")
            String checkId,
            @JsonProperty("check_creation_date")
            LocalDateTime checkCreationDate,
            @JsonProperty("last_screened_date")
            LocalDateTime lastScreenedDate,

            @JsonProperty("created_by")
            String createdBy,

            List<PepData> results
    ) {


        public record PepData(

                @JsonProperty("_id")
                String id,

                String type,

                String externalId,

                @JsonProperty("__v")
                int version,

                List<String> addresses,
                List<String> aliases,

                @JsonProperty("birth_date") // Semi colon separated birth year
                String birthDate,

                List<String> countries,

                String entityType,
                LocalDateTime createdAt,
                LocalDateTime updatedAt,
                String name,

                Boolean deceased,
                String photo,

                String gender,

                List<Position> positions,

                List<String> tags,

                Integer confidenceScore

        ) {

            public record Position(
                    String title,
                    String startDate,
                    String endDate,

                    @JsonProperty("_id")
                    String id
            ) {
            }
        }
    }

    public record EntityDataSanction(
            @JsonProperty("check_id")
            String checkId,
            @JsonProperty("check_creation_date")
            LocalDateTime checkCreationDate,
            @JsonProperty("last_screened_date")
            LocalDateTime lastScreenedDate,

            @JsonProperty("created_by")
            String createdBy,

            List<SanctionData> results
    ) {

        public record SanctionData(

                @JsonProperty("_id")
                String id,

                String type,

                String externalId,

                List<String> addresses,
                List<String> aliases,

                String name,
                @JsonProperty("birth_date") // Semi colon separated birth year
                String birthDate,
                List<String> sanctions,

                List<String> countries,
                List<String> politicalParty,
                Integer confidenceScore,
                Boolean deceased


        ) {
        }
    }


    public record AdverseMediaDate(
            AdverseInfo info,
            List<AdverseResult> result) {

        public record AdverseInfo(String id, String query, int count, int limit) {
        }

        public record AdverseResult(String title, String link, Keyword keyword,
                                    List<AdverseInformation> adverseInformation) {

            public record Keyword(Float confidenceScore) {
            }

            public record AdverseInformation(String keyword, Float score) {

            }
        }
    }
}
