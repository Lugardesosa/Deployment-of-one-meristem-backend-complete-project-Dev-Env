package org.meristem.oneapp.trustiesservice.domains.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response model for dynamic investment forms. Represents one input field.")
public class FormResponse {

    @Schema(
            description = "Currency options for amount input fields. Key is currency code (e.g., NGN), value contains name/logo etc.",
            example = "{\"NGN\": {\"currencyName\": \"Naira\", \"currencyLogo\": \"https://example.com/naira.png\"}}"
    )
    @Builder.Default
    private List<Currency> currencies = new ArrayList<>();

    @Schema(description = "Holds the forms data")
    private List<FormData> formData;

    @Schema(
            description = "Selection options for select-type fields. Key is selection group, value is list of choices. The key is the field order it belongs to",
            example = "{\"3\": [\"Fixed\", \"Savings\", \"Current\"]}"
    )
    @Builder.Default
    private Map<String, Object> selections = new HashMap<>();

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "Model representing a form data")
    public static class FormData {

        @Schema(
                description = "The assetType of the form (e.g., 1, 2, 3, 4)",
                example = "1"
        )
        @JsonIgnore
        private Long id;

        @Schema(
                description = "The position of the form on the UI (e.g., 1, 2, 3, 4)",
                example = "2"
        )
        @JsonIgnore
        private String formPosition;

        @Schema(
                description = "The placeholder text shown inside the input field",
                example = "Enter investment amount"
        )
        private String placeholder;

        @Schema(
                description = "Additional help text or subtext displayed under the field",
                example = "Minimum amount is ₦1,000"
        )
        private String subtext;

        @Schema(
                description = "The type of form field. Examples: 'text', 'select', 'number', 'date'",
                example = "number"
        )
        private String type;

        @Schema(
                description = "The display order of the field in the form",
                example = "2"
        )
        private Integer fieldOrder;

        @Schema(
                description = "The display number of this page for the form",
                example = "1"
        )
        private Integer pageNo;

        @Schema(
                description = "The visible label of the field",
                example = "Amount"
        )
        private String label;

        @Schema(
                description = "The default value that appears in the field before user input",
                example = "10000"
        )
        private String defaultValue;

        @Schema(
                description = "The font/text size of the field content (in px or rem depending on client)",
                example = "14"
        )
        private Integer textSize;

        @Schema(
                description = "Specifies if the field is required (1 = mandatory, 0 = optional)",
                example = "1"
        )
        private Integer mandatory;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "Model representing a currency used in the form")
    public static class Currency {
        @Schema(
                description = "Name of the currency",
                example = "Naira"
        )
        private String currencyName;

        @Schema(
                description = "URL or path to the logo of the currency",
                example = "https://example.com/naira.png"
        )
        private String currencyLogo;

        @Schema(
                description = "Id of the currency",
                example = "1"
        )
        private Long currencyId;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "Model representing a selection used in the form")
    public static class Selection {

        @Schema(
                description = "Each of the values in the selection",
                example = "Cash"
        )
        private String selectionValue;

        @Schema(
                description = "Any other value, label of each category in the case of 'Asset Category'",
                example = "Account Number"
        )
        private String additionalValue;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "Model representing a phone number country code and logo used in the form")
    public static class PhoneCode {
        @Schema(
                description = "Country code",
                example = "+234"
        )
        private String countryCode;

        @Schema(
                description = "URL or path to the logo of the country",
                example = "https://example.com/nigeria.png"
        )
        private String countryLogo;
    }
}
