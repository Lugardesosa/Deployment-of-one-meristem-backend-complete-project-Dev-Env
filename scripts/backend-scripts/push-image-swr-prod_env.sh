#!/bin/bash
set -e

echo "Using image tag: $IMAGE_TAG"
echo "prod_env: $environment"


SVC_NAMES=(users-service notification-service cloud-gateway config-server wallet-service report-service trustees-service)

# Step 1: Authenticate with Huawei Cloud SWR
echo "Logging into Huawei SWR..."
docker login -u ${SWR_REGION}@${HUAWEI_SWR_USERNAME} -p ${HUAWEI_SWR_PASSWORD} ${SWR_REGISTRY_URL}


# Step 2: Tag and Push each image to Huawei SWR
for SERVICE in "${SVC_NAMES[@]}"; do
  echo "Tagging and pushing $SERVICE..."
  docker tag ${SERVICE}:${IMAGE_TAG} ${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SERVICE}:${IMAGE_TAG}
  docker push ${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SERVICE}:${IMAGE_TAG}


  # Update values-${environment}.yaml in the chart
  VALUES_FILE="${CHARTS_DIR}/${SERVICE}/values-${environment}.yaml"

  if [[ -f "$VALUES_FILE" ]]; then
    echo "Updating image in ${VALUES_FILE}..."
    yq e -i ".image.repository = \"${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SERVICE}\"" "$VALUES_FILE"
    yq e -i ".image.tag = \"${IMAGE_TAG}\"" "$VALUES_FILE"
  else
    echo "❌ ERROR: Values file ${VALUES_FILE} not found! Exiting..."
    exit 1
  fi
done


# Step 3: Completion Message
echo "All services built and pushed successfully!"
