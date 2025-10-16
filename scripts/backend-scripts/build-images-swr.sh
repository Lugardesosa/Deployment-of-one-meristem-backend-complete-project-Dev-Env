#!/bin/bash
set -e

# ----------------------------------------
# Setup IMAGE TAG
# ----------------------------------------
if [ -z "$IMAGE_TAG" ]; then
  IMAGE_TAG=$(echo "${GITHUB_SHA}" | cut -c1-7)-${GITHUB_RUN_NUMBER}
fi
echo "Using image tag: $IMAGE_TAG"

export IMAGE_TAG

# ----------------------------------------
# Install Docker if missing
# ----------------------------------------
echo "Checking dependencies for Docker..."
sudo apt update -y
sudo apt install -y git curl wget apt-transport-https ca-certificates gnupg lsb-release software-properties-common

if ! command -v docker &> /dev/null; then
  echo "Installing Docker..."
  curl -fsSL https://get.docker.com | sudo bash
  sudo groupadd docker || true
  sudo usermod -aG docker $USER
  echo "Docker installed."
else
  echo "Docker already installed."
fi

# ----------------------------------------
# Install pack CLI
# ----------------------------------------
echo "Installing pack CLI..."
sudo add-apt-repository -y ppa:cncf-buildpacks/pack-cli
sudo apt-get update -y
sudo apt-get install -y pack-cli

# ----------------------------------------
# Detect Changed Services (PR aware)
# ----------------------------------------
SVC_NAMES=(users-service notification-service cloud-gateway consul config-server wallet-service report-service trustees-service)
BASE_PATH=backend
PACK_BUILDER=paketobuildpacks/builder-jammy-base

# Fetch both base and head branches
echo "Fetching base (${BASE_BRANCH}) and head (${HEAD_BRANCH}) branches..."
git fetch origin "${BASE_BRANCH}" "${HEAD_BRANCH}"

# Detect changes between PR source and destination branches
CHANGED_SERVICES=$(git diff --name-only origin/${BASE_BRANCH}...origin/${HEAD_BRANCH} | grep "^backend/" | cut -d/ -f2 | sort -u)

# If no specific changes detected, build all (first run or new branch)
if [ -z "$CHANGED_SERVICES" ]; then
  echo "No specific changes detected — building all services."
  CHANGED_SERVICES="${SVC_NAMES[@]}"
else
  echo "Changed services detected: $CHANGED_SERVICES"
fi

# ----------------------------------------
# Build only changed microservices
# ----------------------------------------
cd ${BASE_PATH}

for SERVICE in $CHANGED_SERVICES; do
  if [ -d "$SERVICE" ]; then
    echo "Building image for $SERVICE..."
    cd $SERVICE
    sudo pack build $SERVICE \
      --builder ${PACK_BUILDER} \
      --path . \
      --tag ${SERVICE}:${IMAGE_TAG}
    cd ..
  else
    echo "Directory not found for $SERVICE, skipping."
  fi
done

cd .. # Return to root
