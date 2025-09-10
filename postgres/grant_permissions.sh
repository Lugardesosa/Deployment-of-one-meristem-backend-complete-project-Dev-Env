#!/bin/bash
set -e

# Config
POSTGRES_CONTAINER="postgres"
ADMIN_USER="admin"

# List of services (databases + roles)
SERVICES=(
  "users-service"
  "notification-service"
  "report-service"
  "wallet-service"
  "trusties-service"
)

for SERVICE in "${SERVICES[@]}"; do
  echo ">>> Applying grants for database: $SERVICE"

  docker exec -i $POSTGRES_CONTAINER psql -U $ADMIN_USER -d "$SERVICE" <<EOF
GRANT USAGE, CREATE ON SCHEMA public TO "$SERVICE";
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO "$SERVICE";
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO "$SERVICE";
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO "$SERVICE";
EOF

  echo ">>> Done with $SERVICE"
done
