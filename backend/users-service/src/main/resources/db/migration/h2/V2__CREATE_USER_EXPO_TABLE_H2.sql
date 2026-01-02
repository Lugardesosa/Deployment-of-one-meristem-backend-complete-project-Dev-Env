SET @RolesUserID = (SELECT (id) FROM roles WHERE name = 'USER');

INSERT INTO permissions (created_date, created_by, last_modified_date, last_modified_by, version, status, name)
VALUES
    (NOW(), 'SYSTEM', NOW(), 'SYSTEM', 0, 1, 'users.device.register');

SET @UsersDeviceRegisterID = (SELECT id FROM permissions WHERE name = 'users.device.register');

INSERT INTO permissions_mapping (roles_id, permissions_id)
VALUES (@RolesUserID, @UsersDeviceRegisterID);