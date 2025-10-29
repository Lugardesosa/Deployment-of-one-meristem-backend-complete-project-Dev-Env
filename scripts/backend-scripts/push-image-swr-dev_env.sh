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
# Read changed services and convert to array
# ----------------------------------------
LINE=$(cat build_output/changed_services.txt | tr -d '\r\n')
IFS=' ' read -r -a SERVICES_ARRAY <<< "$LINE"

echo "DEBUG: Raw changed_services.txt content:"
cat build_output/changed_services.txt
echo "DEBUG: Services array parsed from changed_services.txt:"
printf '  - %s\n' "${SERVICES_ARRAY[@]}"
echo "Using IMAGE_TAG=$IMAGE_TAG"

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
# Loop over array and push images
# ----------------------------------------
for SERVICE in "${SERVICES_ARRAY[@]}"; do
  echo "------------------------------"
  echo "Processing service: $SERVICE"

  if ! docker image inspect "${SERVICE}:${IMAGE_TAG}" > /dev/null 2>&1; then
    echo "Image ${SERVICE}:${IMAGE_TAG} not found locally — skipping."
    continue
  fi

  echo "Tagging image ${SERVICE}:${IMAGE_TAG}..."
  docker tag "${SERVICE}:${IMAGE_TAG}" "${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SERVICE}:${IMAGE_TAG}"

  echo "Pushing image ${SERVICE}:${IMAGE_TAG} to SWR..."
  docker push "${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SERVICE}:${IMAGE_TAG}"

  # Update Helm values
  VALUES_FILE="${HELM_MOBILE_REPO_PATH}/${SERVICE}/values-${BRANCH_ENV}.yaml"
  if [[ -f "$VALUES_FILE" ]]; then
      echo "Updating Helm values for ${SERVICE}..."
      yq e -i ".image.repository = \"${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SERVICE}\"" "$VALUES_FILE"
      yq e -i ".image.tag = \"${IMAGE_TAG}\"" "$VALUES_FILE"

      # Debug: print updated values
      UPDATED_REPO=$(yq e '.image.repository' "$VALUES_FILE")
      UPDATED_TAG=$(yq e '.image.tag' "$VALUES_FILE")
      echo "DEBUG: ${VALUES_FILE} updated:"
      echo "  image.repository = $UPDATED_REPO"
      echo "  image.tag        = $UPDATED_TAG"
  else
      echo "Values file ${VALUES_FILE} not found, skipping Helm update."
  fi
done

echo "All detected images processed successfully!"
