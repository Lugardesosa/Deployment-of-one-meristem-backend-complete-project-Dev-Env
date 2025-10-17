#!/bin/bash
set -e

# ----------------------------------------
# Load metadata
# ----------------------------------------
if [ -f "build_output/image_metadata.env" ]; then
  # Fix invalid variable names before sourcing
  sed -i 's/-/_/g' build_output/image_metadata.env
  source build_output/image_metadata.env
else
  echo "image_metadata.env not found! Exiting..."
  exit 1
fi

# ----------------------------------------
# Ensure changed services file exists
# ----------------------------------------
if [ ! -f "build_output/changed_services.txt" ] && [ ! -f "build_output/changed_services" ]; then
  echo "changed_services file not found! Exiting..."
  exit 1
fi

CHANGED_SERVICES=$(cat build_output/changed_services.txt || cat build_output/changed_services)
echo "Pushing services: $CHANGED_SERVICES"
echo "Using image tag: $IMAGE_TAG"

# ----------------------------------------
# Authenticate with Huawei Cloud SWR
# ----------------------------------------
echo "Logging into Huawei SWR..."
docker login -u "${SWR_REGION}@${HUAWEI_SWR_USERNAME}" -p "${HUAWEI_SWR_PASSWORD}" "${SWR_REGISTRY_URL}"

# ----------------------------------------
# Tag and Push only built images
# ----------------------------------------
for SERVICE in $CHANGED_SERVICES; do
  echo "Processing $SERVICE..."
  
  # Check if image exists locally before pushing
  if ! docker image inspect ${SERVICE}:${IMAGE_TAG} > /dev/null 2>&1; then
    echo "Image ${SERVICE}:${IMAGE_TAG} not found locally — skipping."
    continue
  fi

  echo "Tagging image ${SERVICE}:${IMAGE_TAG}..."
  docker tag ${SERVICE}:${IMAGE_TAG} ${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SERVICE}:${IMAGE_TAG}

  echo "Pushing image to SWR..."
  docker push ${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SERVICE}:${IMAGE_TAG}


# ----------------------------------------
# Update Helm values
# ----------------------------------------
VALUES_FILE="${HELM_MOBILE_REPO_PATH}/${SERVICE}/values-${BRANCH_ENV}.yaml"
if [[ -f "$VALUES_FILE" ]]; then
  echo "Updating Helm values for ${SERVICE}..."
  yq e -i ".image.repository = \"${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SERVICE}\"" "$VALUES_FILE"
  yq e -i ".image.tag = \"${IMAGE_TAG}\"" "$VALUES_FILE"
else
  echo "Values file ${VALUES_FILE} not found, skipping Helm update."
fi

done

echo "All detected images processed successfully!"
