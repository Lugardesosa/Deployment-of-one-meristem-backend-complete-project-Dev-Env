
DO
$$
    DECLARE
        RolesUserID INT;
        UsersDeviceRegisterID INT;
    BEGIN
        SELECT id INTO RolesUserID FROM roles WHERE name = 'USER';

        INSERT INTO permissions (created_date, created_by, last_modified_date, last_modified_by, version, status, name)
        VALUES
            (NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0, 1, 'users.device.register') RETURNING id INTO UsersDeviceRegisterID;

        INSERT INTO roles_permissions (roles_id, permissions_id)
        VALUES (RolesUserID, UsersDeviceRegisterID);
END
$$;