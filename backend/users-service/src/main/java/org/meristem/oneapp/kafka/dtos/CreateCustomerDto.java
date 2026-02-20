package org.meristem.oneapp.kafka.dtos;

import lombok.Builder;

@Builder
public record CreateCustomerDto(
        String firstName,
        String lastName,
        String middleName,
        String email,
        Long userId,
        String phoneNumber,
        String dateOfBirth,
        String nationalId,
        String address,
        String employerName,
        String gender,
        String occupation) {
}
