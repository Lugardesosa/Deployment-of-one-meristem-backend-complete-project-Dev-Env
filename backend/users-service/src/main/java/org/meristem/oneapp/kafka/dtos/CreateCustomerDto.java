package org.meristem.oneapp.kafka.dtos;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CreateCustomerDto(
        String firstName,
        String lastName,
        String middleName,
        String email,
        Long userId,
        String phoneNumber,
        String nationalId,
        String address,
        String employerName,
        String gender,
        String occupation,
        String addressStreet,
        String addressCity,
        String addressCountryCd,
        String bankBvnNo,
        String parentCustomerId,
        Long parentUserId,
        LocalDate birthDate
) {
}
