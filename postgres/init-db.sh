#!/bin/bash
set -euo pipefail

# =============================
# Validate required env variables
# =============================
required_vars=(
  DB_NAME_USERS_SERVICE DB_USERS_USERNAME DB_USERS_PASSWORD
  DB_NAME_NOTIFICATION_SERVICE DB_NOTIFICATION_USERNAME DB_NOTIFICATION_PASSWORD
  DB_NAME_WALLET_SERVICE DB_WALLET_USERNAME DB_WALLET_PASSWORD
  DB_NAME_REPORT_SERVICE DB_REPORT_USERNAME DB_REPORT_PASSWORD
  DB_NAME_TRUSTEES_SERVICE DB_TRUSTEES_USERNAME DB_TRUSTEES_PASSWORD
  DB_NAME_WEALTH_SERVICE DB_WEALTH_USERNAME DB_WEALTH_PASSWORD
)

for v in "${required_vars[@]}"; do
  if [ -z "${!v:-}" ]; then
    echo "Missing required env var: $v"
    exit 1
  fi
done

POSTGRES_USER="${POSTGRES_USER:-admin}"

# =============================
# Functions
# =============================

# Create database if it doesn't exist
create_db_if_not_exists() {
  local dbname="$1"
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" -tAc "SELECT 1 FROM pg_database WHERE datname='$dbname'" | grep -q 1 || \
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" -c "CREATE DATABASE \"$dbname\";"
}

# Create user if it doesn't exist
create_user_if_not_exists() {
  local username="$1"
  local password="$2"
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" -tAc "SELECT 1 FROM pg_roles WHERE rolname='$username'" | grep -q 1 || \
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" -c "CREATE USER \"$username\" WITH PASSWORD '$password';"
}

# Grant privileges safely
grant_privileges() {
  local dbname="$1"
  local username="$2"
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" -c "GRANT CONNECT ON DATABASE \"$dbname\" TO \"$username\";"
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="$dbname" -c "GRANT USAGE, CREATE ON SCHEMA public TO \"$username\";"
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="$dbname" -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO \"$username\";"
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="$dbname" -c "GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO \"$username\";"
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="$dbname" -c "GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO \"$username\";"
}

# =============================
# Users Service
# =============================
create_db_if_not_exists "${DB_NAME_USERS_SERVICE}"
create_user_if_not_exists "${DB_USERS_USERNAME}" "${DB_USERS_PASSWORD}"
grant_privileges "${DB_NAME_USERS_SERVICE}" "${DB_USERS_USERNAME}"

# =============================
# Notification Service
# =============================
create_db_if_not_exists "${DB_NAME_NOTIFICATION_SERVICE}"
create_user_if_not_exists "${DB_NOTIFICATION_USERNAME}" "${DB_NOTIFICATION_PASSWORD}"
grant_privileges "${DB_NAME_NOTIFICATION_SERVICE}" "${DB_NOTIFICATION_USERNAME}"

# =============================
# Wallet Service
# =============================
create_db_if_not_exists "${DB_NAME_WALLET_SERVICE}"
create_user_if_not_exists "${DB_WALLET_USERNAME}" "${DB_WALLET_PASSWORD}"
grant_privileges "${DB_NAME_WALLET_SERVICE}" "${DB_WALLET_USERNAME}"

# =============================
# Report Service
# =============================
create_db_if_not_exists "${DB_NAME_REPORT_SERVICE}"
create_user_if_not_exists "${DB_REPORT_USERNAME}" "${DB_REPORT_PASSWORD}"
grant_privileges "${DB_NAME_REPORT_SERVICE}" "${DB_REPORT_USERNAME}"

# =============================
# Trustees Service
# =============================
create_db_if_not_exists "${DB_NAME_TRUSTEES_SERVICE}"
create_user_if_not_exists "${DB_TRUSTEES_USERNAME}" "${DB_TRUSTEES_PASSWORD}"
grant_privileges "${DB_NAME_TRUSTEES_SERVICE}" "${DB_TRUSTEES_USERNAME}"

# =============================
# Wealth Service
# =============================
create_db_if_not_exists "${DB_NAME_WEALTH_SERVICE}"
create_user_if_not_exists "${DB_WEALTH_USERNAME}" "${DB_WEALTH_PASSWORD}"
grant_privileges "${DB_NAME_WEALTH_SERVICE}" "${DB_WEALTH_USERNAME}"

echo "All databases, users, and privileges configured successfully."



#The Initial Manual Process to fall back on if the above doesnt work
# #!/bin/bash
# set -euo pipefail


# for v in "${required_vars[@]}"; do
#   if [ -z "${!v:-}" ]; then
#     echo "Missing required env var: $v"
#     exit 1
#   fi
# done
# POSTGRES_USER="${POSTGRES_USER:-admin}"

# # =============================
# # Users Service
# # =============================
# psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
#     CREATE DATABASE "${DB_NAME_USERS_SERVICE}";
#     CREATE USER "${DB_USERS_USERNAME}" WITH PASSWORD '${DB_USERS_PASSWORD}';
#     GRANT CONNECT ON DATABASE "${DB_NAME_USERS_SERVICE}" TO "${DB_USERS_USERNAME}";
#     GRANT ALL PRIVILEGES ON DATABASE "${DB_NAME_USERS_SERVICE}" TO "${DB_USERS_USERNAME}";
# EOSQL

# psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="${DB_NAME_USERS_SERVICE}" <<-EOSQL
#     GRANT USAGE, CREATE ON SCHEMA public TO "${DB_USERS_USERNAME}";
#     GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO "${DB_USERS_USERNAME}";
#     GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO "${DB_USERS_USERNAME}";
#     GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO "${DB_USERS_USERNAME}";
# EOSQL


# # =============================
# # Notification Service
# # =============================
# psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
#     CREATE DATABASE "${DB_NAME_NOTIFICATION_SERVICE}";
#     CREATE USER "${DB_NOTIFICATION_USERNAME}" WITH PASSWORD '${DB_NOTIFICATION_PASSWORD}';
#     GRANT CONNECT ON DATABASE "${DB_NAME_NOTIFICATION_SERVICE}" TO "${DB_NOTIFICATION_USERNAME}";
#     GRANT ALL PRIVILEGES ON DATABASE "${DB_NAME_NOTIFICATION_SERVICE}" TO "${DB_NOTIFICATION_USERNAME}";
# EOSQL

# psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="${DB_NAME_NOTIFICATION_SERVICE}" <<-EOSQL
#     GRANT USAGE, CREATE ON SCHEMA public TO "${DB_NOTIFICATION_USERNAME}";
#     GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO "${DB_NOTIFICATION_USERNAME}";
#     GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO "${DB_NOTIFICATION_USERNAME}";
#     GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO "${DB_NOTIFICATION_USERNAME}";
# EOSQL


# # =============================
# # Wallet Service
# # =============================
# psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
#     CREATE DATABASE "${DB_NAME_WALLET_SERVICE}";
#     CREATE USER "${DB_WALLET_USERNAME}" WITH PASSWORD '${DB_WALLET_PASSWORD}';
#     GRANT CONNECT ON DATABASE "${DB_NAME_WALLET_SERVICE}" TO "${DB_WALLET_USERNAME}";
#     GRANT ALL PRIVILEGES ON DATABASE "${DB_NAME_WALLET_SERVICE}" TO "${DB_WALLET_USERNAME}";
# EOSQL

# psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="${DB_NAME_WALLET_SERVICE}" <<-EOSQL
#     GRANT USAGE, CREATE ON SCHEMA public TO "${DB_WALLET_USERNAME}";
#     GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO "${DB_WALLET_USERNAME}";
#     GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO "${DB_WALLET_USERNAME}";
#     GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO "${DB_WALLET_USERNAME}";
# EOSQL


# # =============================
# # Report Service
# # =============================
# psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
#     CREATE DATABASE "${DB_NAME_REPORT_SERVICE}";
#     CREATE USER "${DB_REPORT_USERNAME}" WITH PASSWORD '${DB_REPORT_PASSWORD}';
#     GRANT CONNECT ON DATABASE "${DB_NAME_REPORT_SERVICE}" TO "${DB_REPORT_USERNAME}";
#     GRANT ALL PRIVILEGES ON DATABASE "${DB_NAME_REPORT_SERVICE}" TO "${DB_REPORT_USERNAME}";
# EOSQL

# psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="${DB_NAME_REPORT_SERVICE}" <<-EOSQL
#     GRANT USAGE, CREATE ON SCHEMA public TO "${DB_REPORT_USERNAME}";
#     GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO "${DB_REPORT_USERNAME}";
#     GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO "${DB_REPORT_USERNAME}";
#     GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO "${DB_REPORT_USERNAME}";
# EOSQL


# # =============================
# # Trustees Service
# # =============================
# psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
#     CREATE DATABASE "${DB_NAME_TRUSTEES_SERVICE}";
#     CREATE USER "${DB_TRUSTEES_USERNAME}" WITH PASSWORD '${DB_TRUSTEES_PASSWORD}';
#     GRANT CONNECT ON DATABASE "${DB_NAME_TRUSTEES_SERVICE}" TO "${DB_TRUSTEES_USERNAME}";
#     GRANT ALL PRIVILEGES ON DATABASE "${DB_NAME_TRUSTEES_SERVICE}" TO "${DB_TRUSTEES_USERNAME}";
# EOSQL

# psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="${DB_NAME_TRUSTEES_SERVICE}" <<-EOSQL
#     GRANT USAGE, CREATE ON SCHEMA public TO "${DB_TRUSTEES_USERNAME}";
#     GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO "${DB_TRUSTEES_USERNAME}";
#     GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO "${DB_TRUSTEES_USERNAME}";
#     GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO "${DB_TRUSTEES_USERNAME}";
# EOSQL


# # =============================
# # Wealth Service
# # =============================
# psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
#     CREATE DATABASE "${DB_NAME_WEALTH_SERVICE}";
#     CREATE USER "${DB_WEALTH_USERNAME}" WITH PASSWORD '${DB_WEALTH_PASSWORD}';
#     GRANT CONNECT ON DATABASE "${DB_NAME_WEALTH_SERVICE}" TO "${DB_WEALTH_USERNAME}";
#     GRANT ALL PRIVILEGES ON DATABASE "${DB_NAME_WEALTH_SERVICE}" TO "${DB_WEALTH_USERNAME}";
# EOSQL

# psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="${DB_NAME_WEALTH_SERVICE}" <<-EOSQL
#     GRANT USAGE, CREATE ON SCHEMA public TO "${DB_WEALTH_USERNAME}";
#     GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO "${DB_WEALTH_USERNAME}";
#     GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO "${DB_WEALTH_USERNAME}";
#     GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO "${DB_WEALTH_USERNAME}";
# EOSQL

# echo "All databases, users, and privileges configured successfully."

