package org.meristem.oneapp.trustiesservice.controllers;

import lombok.Builder;

@Builder
public record AssetDeleteResponse(String message, boolean status) {
}
