package org.meristem.oneapp.usersservice.dtos.sql;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

@Slf4j
public class UserResponseResultSetExtractor implements ResultSetExtractor<UsersResponse> {
    @Override
    public UsersResponse extractData(@NonNull ResultSet rs) throws SQLException, DataAccessException {

        if (!rs.next()) {
            return null;
        }

        UsersResponse user = UsersResponse.builder()
                .id(rs.getLong("id"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .email(rs.getString("email"))
                .phoneNumber(rs.getString("phone_number"))
                .userInstrumentResponses(new ArrayList<>())
                .onboardingCompleted(rs.getBoolean("onboarding_completed"))
                .referralCode(rs.getString("referral_code"))
                .middleName(rs.getString("middle_name"))
                .status(rs.getInt("status"))
                .image(rs.getString("image_key"))
                .gender(rs.getString("gender"))
                .dateOfBirth(rs.getObject("date_of_birth", LocalDate.class))
                .pin(rs.getString("pin"))
                .password(rs.getString("password"))
                .biometricEnabled(rs.getBoolean("biometric_enabled"))
                .passwordAttempt(rs.getInt("password_attempt"))
                .build();
        do {
            user.userInstrumentResponses().add(UsersResponse.UserInstrumentResponse.builder()
                    .name(rs.getString("name")).id(rs.getLong("iiid"))
                    .code(rs.getString("code"))
                    .accessed(rs.getBoolean("accessed"))
                    .build());
        } while (rs.next());
        return user;
    }
}

