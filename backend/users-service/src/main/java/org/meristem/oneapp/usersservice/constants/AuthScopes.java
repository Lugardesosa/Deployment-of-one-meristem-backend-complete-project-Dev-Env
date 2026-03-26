package org.meristem.oneapp.usersservice.constants;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public final class AuthScopes {

    public static final String GET_BENEFICIARIES = "beneficiaries.get";
    public static final String GET_USERS = "users.get";
    public static final String GET_ROLES = "roles.get";
    public static final String GET_PERMISSIONS = "permissions.get";
    public static final String GET_SIGNED_URL = "generate_signed_url";
    public static final String VERIFY_PIN = "verify.pin";
    public static final String GET_CUSTOMER_ID = "user.customer_ids";
    public static final String GET_USER_ID = "user.user_id";
    public static final List<String> CORE_SCOPES = List.of(VERIFY_PIN, GET_CUSTOMER_ID, GET_USER_ID, GET_BENEFICIARIES, GET_USERS);
}
