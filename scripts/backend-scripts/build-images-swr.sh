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
echo "Checking Docker and dependency setup..."
echo "Checking Docker and dependency setup..."

if ! command -v docker &> /dev/null; then
  echo "Docker not found — installing required dependencies and Docker..."

  # Install dependencies if missing
  if ! dpkg -s git curl wget apt-transport-https ca-certificates gnupg lsb-release software-properties-common &> /dev/null; then
    echo "Installing missing base dependencies..."
    sudo apt update -y
    sudo apt install -y git curl wget apt-transport-https ca-certificates gnupg lsb-release software-properties-common
  else
    echo "Base dependencies already installed — skipping."
  fi

  echo "Docker not found — installing required dependencies and Docker..."

  # Install dependencies if missing
  if ! dpkg -s git curl wget apt-transport-https ca-certificates gnupg lsb-release software-properties-common &> /dev/null; then
    echo "Installing missing base dependencies..."
    sudo apt update -y
    sudo apt install -y git curl wget apt-transport-https ca-certificates gnupg lsb-release software-properties-common
  else
    echo "Base dependencies already installed — skipping."
  fi

  echo "Installing Docker..."
  curl -fsSL https://get.docker.com | sudo bash
  sudo groupadd docker || true
  sudo usermod -aG docker $USER
  echo "Docker installation complete."
  echo "Docker installation complete."
else
  echo "Docker already installed."
fi



# ----------------------------------------
# Install pack CLI for build
# ----------------------------------------
echo "Checking if pack CLI is already installed..."
if command -v pack &> /dev/null; then
  echo "pack CLI already installed — skipping installation."
  echo "pack CLI already installed — skipping installation."
else
  echo "Installing pack CLI..."
  sudo add-apt-repository -y ppa:cncf-buildpacks/pack-cli
  sudo apt-get update -y
  sudo apt-get install -y pack-cli
  echo "pack CLI installation complete."
fi


# ----------------------------------------
# Detect Changed Services (Smart diff + new folder aware)
# Detect Changed Services (Smart diff + new folder aware)
# ----------------------------------------
SVC_NAMES=(users-service notification-service cloud-gateway config-server wallet-service report-service trustees-service)
BASE_PATH=backend
PACK_BUILDER=paketobuildpacks/builder-jammy-base

echo "----------------------------------------"
echo "Detecting changed microservices for build..."
echo "----------------------------------------"

# Auto-detect branches if not set
BASE_BRANCH=${BASE_BRANCH:-${GITHUB_BASE_REF:-"main"}}
HEAD_BRANCH=${HEAD_BRANCH:-${GITHUB_HEAD_REF:-$(git rev-parse --abbrev-ref HEAD)}}

echo "Base branch: ${BASE_BRANCH}"
echo "Head branch: ${HEAD_BRANCH}"

# Fetch both branches for comparison
git fetch origin "${BASE_BRANCH}" "${HEAD_BRANCH}" --quiet
echo "----------------------------------------"
echo "Detecting changed microservices for build..."
echo "----------------------------------------"

# Auto-detect branches if not set
BASE_BRANCH=${BASE_BRANCH:-${GITHUB_BASE_REF:-"main"}}
HEAD_BRANCH=${HEAD_BRANCH:-${GITHUB_HEAD_REF:-$(git rev-parse --abbrev-ref HEAD)}}

echo "Base branch: ${BASE_BRANCH}"
echo "Head branch: ${HEAD_BRANCH}"

# Fetch both branches for comparison
git fetch origin "${BASE_BRANCH}" "${HEAD_BRANCH}" --quiet

# ----------------------------------------
# STEP 1: Detect backend differences between branches
# STEP 1: Detect backend differences between branches
# ----------------------------------------
CHANGED_SERVICES=$(git diff --name-only "origin/${BASE_BRANCH}"..."origin/${HEAD_BRANCH}" | grep "^backend/" | cut -d/ -f2 | sort -u || true)

echo "Files changed under backend/:"
git diff --name-only "origin/${BASE_BRANCH}"..."origin/${HEAD_BRANCH}" | grep '^backend/' || echo "No files changed under backend/"

# ----------------------------------------
# STEP 2: Handle case when no changes found
# ----------------------------------------
CHANGED_SERVICES=$(git diff --name-only "origin/${BASE_BRANCH}"..."origin/${HEAD_BRANCH}" | grep "^backend/" | cut -d/ -f2 | sort -u || true)

echo "Files changed under backend/:"
git diff --name-only "origin/${BASE_BRANCH}"..."origin/${HEAD_BRANCH}" | grep '^backend/' || echo "No files changed under backend/"

# ----------------------------------------
# STEP 2: Handle case when no changes found
# ----------------------------------------
if [ -z "$CHANGED_SERVICES" ]; then
  echo "No backend diffs detected — checking for merge base or new branch..."
  if git merge-base --is-ancestor "origin/${BASE_BRANCH}" "origin/${HEAD_BRANCH}" 2>/dev/null; then
    echo "Common history exists but no diffs — building all services (safe default)."
    CHANGED_SERVICES="${SVC_NAMES[@]}"
  else
    echo "No merge base found (new branch or first PR to target) — building all services."
    CHANGED_SERVICES="${SVC_NAMES[@]}"
  fi
  echo "No backend diffs detected — checking for merge base or new branch..."
  if git merge-base --is-ancestor "origin/${BASE_BRANCH}" "origin/${HEAD_BRANCH}" 2>/dev/null; then
    echo "Common history exists but no diffs — building all services (safe default)."
    CHANGED_SERVICES="${SVC_NAMES[@]}"
  else
    echo "No merge base found (new branch or first PR to target) — building all services."
    CHANGED_SERVICES="${SVC_NAMES[@]}"
  fi
else
  echo "Changed services detected: $CHANGED_SERVICES"
fi

# ----------------------------------------
# STEP 3: Detect newly added backend folders
# ----------------------------------------
for svc in $(ls ${BASE_PATH}); do
  if [ -d "${BASE_PATH}/${svc}" ]; then
    if [[ ! " ${SVC_NAMES[@]} " =~ " ${svc} " ]]; then
      echo "New microservice folder detected: ${svc} — adding to build list."
      SVC_NAMES+=("$svc")
      CHANGED_SERVICES+=" ${svc}"
    fi
  fi
done

# ----------------------------------------
# Final service list for build
# ----------------------------------------
if [ -z "$CHANGED_SERVICES" ]; then
  echo "No specific services detected — building all."
  CHANGED_SERVICES="${SVC_NAMES[@]}"
fi

echo "----------------------------------------"
echo "Services selected for build: $CHANGED_SERVICES"
echo "----------------------------------------"

# ----------------------------------------
# STEP 3: Detect newly added backend folders
# ----------------------------------------
for svc in $(ls ${BASE_PATH}); do
  if [ -d "${BASE_PATH}/${svc}" ]; then
    if [[ ! " ${SVC_NAMES[@]} " =~ " ${svc} " ]]; then
      echo "New microservice folder detected: ${svc} — adding to build list."
      SVC_NAMES+=("$svc")
      CHANGED_SERVICES+=" ${svc}"
    fi
  fi
done

# ----------------------------------------
# Final service list for build
# ----------------------------------------
if [ -z "$CHANGED_SERVICES" ]; then
  echo "No specific services detected — building all."
  CHANGED_SERVICES="${SVC_NAMES[@]}"
fi

echo "----------------------------------------"
echo "Services selected for build: $CHANGED_SERVICES"
echo "----------------------------------------"

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
      # Service changed or new → build fresh
      # Service changed or new → build fresh
      SERVICE_TAG="${IMAGE_TAG}"
      echo "Detected changes in $SERVICE → building new image ($SERVICE_TAG)"
      cd $SERVICE
      sudo pack build $SERVICE \
        --builder ${PACK_BUILDER} \
        --path . \
        --tag ${SERVICE}:${SERVICE_TAG}
      cd ..
    else
      # Reuse previous tag if unchanged
      # Reuse previous tag if unchanged
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

    echo "${SERVICE}_TAG=${SERVICE_TAG}" >> ../build_output/image_metadata.env
  else
    echo "Directory not found for $SERVICE, skipping."
  fi
done

cd .. # back to root
cd .. # back to root

# ----------------------------------------
# Save metadata and changed services
# ----------------------------------------
echo "$CHANGED_SERVICES" > build_output/changed_services.txt
echo "IMAGE_TAG=$IMAGE_TAG" >> build_output/image_metadata.env

echo "Build completed successfully!"
