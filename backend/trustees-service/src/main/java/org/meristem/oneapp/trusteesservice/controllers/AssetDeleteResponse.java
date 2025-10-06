package org.meristem.oneapp.trusteesservice.controllers;

import lombok.Builder;

@Builder
public record AssetDeleteResponse(String message, boolean status) {
}
