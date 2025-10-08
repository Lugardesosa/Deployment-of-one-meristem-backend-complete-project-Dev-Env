#!/bin/bash

set -e

if [ -z "$IMAGE_TAG" ]; then
  IMAGE_TAG=$(echo "${GITHUB_SHA}" | cut -c1-7)-${GITHUB_RUN_NUMBER}
fi
echo "📛 Using image tag: $IMAGE_TAG"

export IMAGE_TAG

# ----------------------------------------
# ✅ Install Docker 
# ----------------------------------------

echo "📦 Checking dependencies for Docker..."

# Install apt dependencies
sudo apt update
sudo apt install -y git curl wget apt-transport-https ca-certificates gnupg lsb-release

# Check & Install Docker
if ! command -v docker &> /dev/null; then
  echo "🐳 Installing Docker..."
  curl -fsSL https://get.docker.com | sudo bash

  sudo groupadd docker || true
  sudo usermod -aG docker $USER
  echo "🔁 Docker installed. Please log out and back in to apply group changes."
else
  echo "Docker already installed. Skipping."
fi

SVC_NAMES=(users-service notification-service cloud-gateway consul config-server wallet-service report-service trustees-service)
PACK_BUILDER=paketobuildpacks/builder-jammy-base
BASE_PATH=backend

echo "Installing pack CLI..."
sudo add-apt-repository -y ppa:cncf-buildpacks/pack-cli
sudo apt-get update
sudo apt-get install -y pack-cli

cd ${BASE_PATH}
for SERVICE in "${SVC_NAMES[@]}"; do
  echo "Building image for $SERVICE..."
  cd $SERVICE
  sudo pack build $SERVICE \
    --builder ${PACK_BUILDER} \
    --path . \
    --tag ${SERVICE}:${IMAGE_TAG}
  cd ..
done
cd .. # Return to root directory
