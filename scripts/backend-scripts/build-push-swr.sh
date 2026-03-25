#!/bin/bash
set -euo pipefail

echo "=================================================="
echo " ONE MERISTEM — BUILD & PUSH TO HUAWEI SWR"
echo "=================================================="

: "${CHANGED_SERVICES:?CHANGED_SERVICES is required}"
: "${IMAGE_TAG:?IMAGE_TAG is required}"
: "${SWR_REGISTRY_URL:?SWR_REGISTRY_URL is required}"
: "${SWR_ORGANIZATION_NAME:?SWR_ORGANIZATION_NAME is required}"
: "${HUAWEI_SWR_USERNAME:?HUAWEI_SWR_USERNAME is required}"
: "${HUAWEI_SWR_PASSWORD:?HUAWEI_SWR_PASSWORD is required}"

echo "IMAGE_TAG        : $IMAGE_TAG"
echo "CHANGED_SERVICES : $CHANGED_SERVICES"
echo "SWR_REGISTRY_URL : $SWR_REGISTRY_URL"
echo "SWR_ORG          : $SWR_ORGANIZATION_NAME"

if [ -z "$CHANGED_SERVICES" ] || [ "$CHANGED_SERVICES" = "none" ]; then
  echo "No changed services to build. Exiting."
  exit 0
fi

# ----------------------------------------
# Ensure Docker is available
# ----------------------------------------
if ! command -v docker &>/dev/null; then
  echo "Docker not found — installing..."
  sudo apt-get update -y
  sudo apt-get install -y \
    curl wget apt-transport-https \
    ca-certificates gnupg lsb-release
  curl -fsSL https://get.docker.com | sudo bash
  sudo usermod -aG docker "$USER"
else
  echo "Docker already installed: $(docker --version)"
fi

# ----------------------------------------
# Ensure pack CLI is available
# ----------------------------------------
if command -v pack &>/dev/null; then
  echo "pack CLI already installed: $(pack --version)"
else
  echo "Installing pack CLI..."
  curl -sSL "https://github.com/buildpacks/pack/releases/download/v0.35.0/pack-v0.35.0-linux.tgz" \
    | sudo tar -C /usr/local/bin/ --no-same-owner -xzv pack
  echo "pack CLI installed: $(pack --version)"
fi

# ----------------------------------------
# Login to Huawei SWR
# ----------------------------------------
echo ""
echo "Logging into Huawei SWR..."
echo "${HUAWEI_SWR_PASSWORD}" | docker login \
  -u "${HUAWEI_SWR_USERNAME}" \
  --password-stdin "${SWR_REGISTRY_URL}"

if [ $? -ne 0 ]; then
  echo "ERROR: Huawei SWR login failed."
  exit 1
fi
echo "SWR login successful."

# ----------------------------------------
# Noble-tiny services (Java 25) — require --volume for /cache
# because the tiny builder has no writable /cache directory.
# Add any new Java 25 noble-tiny service here — this is the
# only place that needs updating when onboarding a new service.
# ----------------------------------------
NOBLE_TINY_SERVICES="wealth-service wallet-service notification-service"

# ----------------------------------------
# Returns builder image and Java version for a given service.
# noble-tiny services → Java 25
# everything else     → Java 21 (jammy-base)
# ----------------------------------------
get_build_config() {
  local service="$1"
  case "$service" in
    wealth-service|wallet-service|notification-service)
      echo "paketobuildpacks/builder-noble-java-tiny:latest 25"
      ;;
    *)
      echo "paketobuildpacks/builder-jammy-base 21"
      ;;
  esac
}

# ----------------------------------------
# Parse services into array
# ----------------------------------------
SERVICES=()
for svc in $CHANGED_SERVICES; do
  [ -z "$svc" ] && continue
  SERVICES+=("$svc")
done

echo ""
echo "Services to build and push: ${SERVICES[*]}"
echo ""

PUSHED=()
SKIPPED=()
FAILED=()

# ----------------------------------------
# Build and push each changed service only
# ----------------------------------------
for svc in "${SERVICES[@]}"; do
  echo "=================================================="
  echo "Processing: $svc"
  echo "=================================================="

  # Gate — only known tracked services proceed
  case "$svc" in
    cloud-gateway|users-service|config-server|notification-service|\
wallet-service|report-service|trustees-service|wealth-service)
      ;;
    *)
      echo "WARN: $svc is not a tracked service — skipping"
      SKIPPED+=("$svc")
      continue
      ;;
  esac

  SERVICE_DIR="backend/${svc}"

  if [ ! -d "$SERVICE_DIR" ]; then
    echo "WARN: Directory $SERVICE_DIR not found — skipping"
    SKIPPED+=("$svc")
    continue
  fi

  if [ ! -f "$SERVICE_DIR/pom.xml" ]; then
    echo "WARN: No pom.xml in $SERVICE_DIR — skipping"
    SKIPPED+=("$svc")
    continue
  fi

  LOCAL_IMAGE="${svc}:${IMAGE_TAG}"
  REMOTE_IMAGE="${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${svc}:${IMAGE_TAG}"

  # Resolve builder and Java version for this specific service
  CONFIG=$(get_build_config "$svc")
  BUILDER=$(echo "$CONFIG" | awk '{print $1}')
  JAVA_VERSION=$(echo "$CONFIG" | awk '{print $2}')

  echo "Builder      : $BUILDER"
  echo "Java Version : $JAVA_VERSION"
  echo "Local Image  : $LOCAL_IMAGE"
  echo "Remote Image : $REMOTE_IMAGE"

  # ----------------------------------------
  # Ensure cache directory exists for all services
  # ----------------------------------------
  sudo mkdir -p "/opt/buildpack-cache/${svc}"
  sudo chmod -R 777 "/opt/buildpack-cache/${svc}"

  echo "Building $LOCAL_IMAGE..."
  cd "${SERVICE_DIR}"

  # ----------------------------------------
  # noble-tiny services need --volume for /cache
  # jammy-base services use standard --cache bind mount
  # ----------------------------------------
  if echo "$NOBLE_TINY_SERVICES" | grep -qw "$svc"; then
    sudo mkdir -p "/opt/buildpack-cache/${svc}/launch"
    if sudo pack build "${svc}" \
        --builder "${BUILDER}" \
        --env BP_JVM_VERSION="${JAVA_VERSION}" \
        --path . \
        --tag "${LOCAL_IMAGE}" \
        --volume "/opt/buildpack-cache/${svc}/launch:/cache" \
        --pull-policy if-not-present; then
      echo "Build successful: $LOCAL_IMAGE"
    else
      echo "ERROR: Build failed for $svc"
      FAILED+=("$svc")
      cd - >/dev/null
      continue
    fi
  else
    if sudo pack build "${svc}" \
        --builder "${BUILDER}" \
        --env BP_JVM_VERSION="${JAVA_VERSION}" \
        --path . \
        --tag "${LOCAL_IMAGE}" \
        --cache "type=build;format=bind;source=/opt/buildpack-cache/${svc}" \
        --pull-policy if-not-present; then
      echo "Build successful: $LOCAL_IMAGE"
    else
      echo "ERROR: Build failed for $svc"
      FAILED+=("$svc")
      cd - >/dev/null
      continue
    fi
  fi

  cd - >/dev/null

  echo "Tagging → $REMOTE_IMAGE"
  docker tag "${LOCAL_IMAGE}" "${REMOTE_IMAGE}"

  echo "Pushing → $REMOTE_IMAGE"
  if docker push "${REMOTE_IMAGE}"; then
    echo "Push successful: $REMOTE_IMAGE"
    PUSHED+=("$svc")
  else
    echo "ERROR: Push failed for $svc"
    FAILED+=("$svc")
    continue
  fi

  echo "Done: $svc"
  echo ""
done

# ----------------------------------------
# Summary
# ----------------------------------------
echo "=================================================="
echo " BUILD & PUSH SUMMARY"
echo "=================================================="
echo "IMAGE_TAG : $IMAGE_TAG"
echo ""

if [ ${#PUSHED[@]} -gt 0 ]; then
  echo "Pushed (${#PUSHED[@]}):"
  for svc in "${PUSHED[@]}"; do
    echo "  - ${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${svc}:${IMAGE_TAG}"
  done
fi

if [ ${#SKIPPED[@]} -gt 0 ]; then
  echo ""
  echo "Skipped (${#SKIPPED[@]}):"
  for svc in "${SKIPPED[@]}"; do
    echo "  - $svc"
  done
fi

if [ ${#FAILED[@]} -gt 0 ]; then
  echo ""
  echo "Failed (${#FAILED[@]}):"
  for svc in "${FAILED[@]}"; do
    echo "  - $svc"
  done
  echo ""
  echo "One or more services failed. See logs above."
  exit 1
fi

echo "=================================================="
echo "All services processed successfully."
echo "=================================================="
