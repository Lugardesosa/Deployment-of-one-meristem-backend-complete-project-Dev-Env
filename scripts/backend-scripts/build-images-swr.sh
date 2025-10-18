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
echo "Checking if pack CLI is already installed..."

if command -v pack &> /dev/null; then
  echo "pack CLI is already installed. Skipping installation..."
else
  echo "Installing pack CLI..."
  sudo add-apt-repository -y ppa:cncf-buildpacks/pack-cli
  sudo apt-get update -y
  sudo apt-get install -y pack-cli
  echo "pack CLI installation complete."
fi


# ----------------------------------------
# Detect Changed Services (PR aware)
# ----------------------------------------
SVC_NAMES=(users-service notification-service cloud-gateway config-server wallet-service report-service trustees-service)
BASE_PATH=backend
PACK_BUILDER=paketobuildpacks/builder-jammy-base

# Fetch both base and head branches
echo "Fetching base (${BASE_BRANCH}) and head (${HEAD_BRANCH}) branches..."
git fetch origin "${BASE_BRANCH}" "${HEAD_BRANCH}"

# ----------------------------------------
# Handle case where branches have no merge base (e.g., new branch)
# ----------------------------------------
if ! git merge-base --is-ancestor "origin/${BASE_BRANCH}" "origin/${HEAD_BRANCH}" 2>/dev/null; then
  echo "No merge base found between ${BASE_BRANCH} and ${HEAD_BRANCH}."
  echo "This is A New branch — so building all microservices..."
  CHANGED_SERVICES="${SVC_NAMES[@]}"
else
  # Detect changes between PR source and destination branches
  CHANGED_SERVICES=$(git diff --name-only origin/${BASE_BRANCH}...origin/${HEAD_BRANCH} | grep "^backend/" | cut -d/ -f2 | sort -u)
fi

# If no specific changes detected, build all (first run or new branch)
if [ -z "$CHANGED_SERVICES" ]; then
  echo "No specific changes detected — building all services."
  CHANGED_SERVICES="${SVC_NAMES[@]}"
else
  echo "Changed services detected: $CHANGED_SERVICES"
fi

# ----------------------------------------
# Export changed services
# ----------------------------------------
echo "$CHANGED_SERVICES" > changed_services.txt
export CHANGED_SERVICES
echo "Exported changed services: $CHANGED_SERVICES"


# ----------------------------------------
# Prepare existing metadata for stable tags
# ----------------------------------------
mkdir -p build_output
METADATA_FILE="build_output/image_metadata.env"

if [ -f "$METADATA_FILE" ]; then
  echo "Found previous image metadata file. Reusing unchanged service tags where applicable..."
else
  echo "No previous metadata file found. All services will get new tags."
fi


# ----------------------------------------
# Build only changed microservices
# ----------------------------------------
cd ${BASE_PATH}

for SERVICE in "${SVC_NAMES[@]}"; do
  if [ -d "$SERVICE" ]; then
    if [[ " $CHANGED_SERVICES " == *" $SERVICE "* ]]; then
      # Service has changed → generate new tag
      SERVICE_TAG="${IMAGE_TAG}"
      echo "Detected changes in $SERVICE → building new image ($SERVICE_TAG)"
      cd $SERVICE
      sudo pack build $SERVICE \
        --builder ${PACK_BUILDER} \
        --path . \
        --tag ${SERVICE}:${SERVICE_TAG}
      cd ..
    else
      # If Service remains unchanged → reuse previous tag if available
      if [ -f "$METADATA_FILE" ]; then
        PREV_TAG=$(grep "^${SERVICE}_TAG=" "$METADATA_FILE" | cut -d'=' -f2)
        if [ -n "$PREV_TAG" ]; then
          SERVICE_TAG="${PREV_TAG}"
          echo "$SERVICE unchanged → reusing previous tag ($SERVICE_TAG)"
        else
          SERVICE_TAG="${IMAGE_TAG}"
          echo "No previous tag found for $SERVICE → using new tag ($SERVICE_TAG)"
        fi
      else
        SERVICE_TAG="${IMAGE_TAG}"
        echo "No metadata file found → using new tag ($SERVICE_TAG)"
      fi
    fi

    # Save service tag for later stages
    echo "${SERVICE}_TAG=${SERVICE_TAG}" >> ../build_output/image_metadata.env
  else
    echo "Directory not found for $SERVICE, skipping."
  fi
done

cd .. # Return to root

# ----------------------------------------
# Save metadata and changed services
# ----------------------------------------
echo "$CHANGED_SERVICES" > build_output/changed_services.txt
echo "IMAGE_TAG=$IMAGE_TAG" >> build_output/image_metadata.env

echo "Build completed successfully!"
