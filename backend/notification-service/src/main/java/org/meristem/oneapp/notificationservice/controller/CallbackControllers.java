package org.meristem.oneapp.notificationservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.notificationservice.constants.ApiConstants;
import org.meristem.oneapp.notificationservice.domains.requests.HollaTagsCallbackRequest;
import org.meristem.oneapp.notificationservice.domains.responses.HollaTagsCallbackResponse;
import org.meristem.oneapp.notificationservice.services.CallbackService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Callback Controllers", description = "Callback Controllers")
@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "callback")
public class CallbackControllers {

    private final CallbackService callbackService;

    @Operation(summary = "Holla Tags callback url")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Holla Tags callback url")
    })
    @PostMapping(value = "/holla-tags", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HollaTagsCallbackResponse hollaTagsCallback(@RequestBody HollaTagsCallbackRequest request) {
        return callbackService.handleHollaTags(request);
    }
}
