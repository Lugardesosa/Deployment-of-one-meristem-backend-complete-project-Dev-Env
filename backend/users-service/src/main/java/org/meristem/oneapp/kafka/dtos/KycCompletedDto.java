package org.meristem.oneapp.kafka.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import org.springframework.data.relational.core.mapping.Column;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record KycCompletedDto(@Column("id") Long userId, @Column("first_name") String firstName, @Column("last_name") String lastName, @Column("phone_number") String phoneNumber,
                              String email, @Column("house_address") String address, @Column("id_value") String bvn, @Column("date_of_birth") String dob, @Column("data_sharing") Boolean dataSharing) {
}
