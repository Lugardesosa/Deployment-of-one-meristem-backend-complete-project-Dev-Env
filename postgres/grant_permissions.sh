#!/bin/bash
set -e

# List of service databases and roles
POSTGRES_USER="admin"
SERVICES=(
  "users-service"
  "notification-service"
  "report-service"
  "wallet-service"
  "trusties-service"
)

for SERVICE in "${SERVICES[@]}"; do
  echo ">>> Applying grants for $SERVICE"

  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$SERVICE" <<-EOSQL
    GRANT USAGE, CREATE ON SCHEMA public TO "$SERVICE";
    GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO "$SERVICE";
    GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO "$SERVICE";
    GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO "$SERVICE";
EOSQL

  echo ">>> Done with $SERVICE"
done
