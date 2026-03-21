package org.meristem.oneapp.usersservice.dtos;

import lombok.Builder;

import java.util.List;
@Builder
public record AdverseMediaDto(String title, String link, Float confidenceScore, List<AdverseInformation> adverseInformation) {

    public record AdverseInformation(String keyword, Float score) {

    }
}
