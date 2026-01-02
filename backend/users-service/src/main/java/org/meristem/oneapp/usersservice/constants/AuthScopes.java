package org.meristem.oneapp.usersservice.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class AuthScopes {

    public static final String GET_BENEFICIARIES = "beneficiaries.get";
    public static final String GET_USERS = "users.get";
    public static final String GET_ROLES = "roles.get";
    public static final String GET_PERMISSIONS = "permissions.get";
    public static final String GET_SIGNED_URL = "generate_signed_url";
}
