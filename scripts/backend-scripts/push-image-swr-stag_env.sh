#!/bin/bash
set -e

# ----------------------------------------
# Ensure changed services file exists
# ----------------------------------------
if [ ! -f "build_output/changed_services.txt" ] && [ ! -f "build_output/changed_services" ]; then
  echo "changed_services file not found! Exiting..."
  exit 1
fi

# ----------------------------------------
# Debug: print arrangement of services
# ----------------------------------------
echo "DEBUG: Raw contents of changed_services.txt:"
cat build_output/changed_services.txt | sed 's/$/\\n/'
echo "DEBUG: Processed services:"
while IFS= read -r SERVICE || [ -n "$SERVICE" ]; do
    SERVICE=$(echo "$SERVICE" | tr -d '\r')
    echo "Service read: '$SERVICE'"
done < build_output/changed_services.txt
echo "DEBUG END"

# ----------------------------------------
# Authenticate with Huawei Cloud SWR
# ----------------------------------------
echo "Logging into Huawei SWR..."
echo "${HUAWEI_SWR_PASSWORD}" | docker login \
  -u "${SWR_REGION}@${HUAWEI_SWR_USERNAME}" \
  --password-stdin "${SWR_REGISTRY_URL}"

if [ $? -ne 0 ]; then
  echo "Huawei SWR login failed. Please check your credentials or token expiration."
  exit 1
else
  echo "Successfully logged into Huawei SWR."
fi

# ----------------------------------------
# Read and process services from changed_services.txt
# ----------------------------------------
echo "Pushing services from changed_services.txt"
while IFS= read -r SERVICE || [ -n "$SERVICE" ]; do
  # Remove any Windows carriage return characters
  SERVICE=$(echo "$SERVICE" | tr -d '\r')
  echo "Processing $SERVICE..."

  # ----------------------------------------
  # Check if Docker image exists
  # ----------------------------------------
  if ! docker image inspect "${SERVICE}:${IMAGE_TAG}" > /dev/null 2>&1; then
    echo "Image ${SERVICE}:${IMAGE_TAG} not found locally — skipping."
    continue
  fi

  # ----------------------------------------
  # Tag and push Docker image
  # ----------------------------------------
  echo "Tagging image ${SERVICE}:${IMAGE_TAG}..."
  docker tag "${SERVICE}:${IMAGE_TAG}" "${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SERVICE}:${IMAGE_TAG}"

  echo "Pushing image ${SERVICE}:${IMAGE_TAG} to SWR..."
  docker push "${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SERVICE}:${IMAGE_TAG}"

  # ----------------------------------------
  # Update Helm values
  # ----------------------------------------
  VALUES_FILE="${HELM_MOBILE_REPO_PATH}/${SERVICE}/values-${BRANCH_ENV}.yaml"
  if [[ -f "$VALUES_FILE" ]]; then
      echo "Updating Helm values for ${SERVICE}..."
      yq e -i ".image.repository = \"${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SERVICE}\"" "$VALUES_FILE"
      yq e -i ".image.tag = \"${IMAGE_TAG}\"" "$VALUES_FILE"

      # Debug: show updated values
      UPDATED_REPO=$(yq e '.image.repository' "$VALUES_FILE")
      UPDATED_TAG=$(yq e '.image.tag' "$VALUES_FILE")
      echo "DEBUG: ${VALUES_FILE} updated:"
      echo "  image.repository = $UPDATED_REPO"
      echo "  image.tag        = $UPDATED_TAG"
  else
      echo "Values file ${VALUES_FILE} not found, skipping Helm update."
  fi

done < build_output/changed_services.txt  # Feed file into while loop

echo "All detected images processed successfully!"
