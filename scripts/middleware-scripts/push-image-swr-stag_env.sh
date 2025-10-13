#!/bin/bash

set -e

echo "Using image tag: $IMAGE_TAG"
echo "stag_env: $environment"


SVC_NAME=middleware-service
export SVC_NAME

#Step 1: Authenticate with Huawei Cloud SWR
echo "Logging into Huawei SWR..."
docker login -u ${SWR_REGION}@${HUAWEI_SWR_USERNAME} -p ${HUAWEI_SWR_PASSWORD} ${SWR_REGISTRY_URL}

#Step 2: Tag and Push the image to Huawei SWR
echo "Tagging and pushing $SVC_NAME..."
docker tag ${SVC_NAME}:${IMAGE_TAG} ${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SVC_NAME}:${IMAGE_TAG}
docker push ${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SVC_NAME}:${IMAGE_TAG}

# Update values-${environment}.yaml in the chart
VALUES_FILE="${HELM_REPO_PATH}/${SVC_NAME}/values-${environment}.yaml"
if [[ -f "$VALUES_FILE" ]]; then
  echo "Updating image in ${VALUES_FILE}..."
  yq e -i ".image.repository = \"${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SVC_NAME}\"" "$VALUES_FILE"
  yq e -i ".image.tag = \"${IMAGE_TAG}\"" "$VALUES_FILE"
else
  echo "ERROR: Values file ${VALUES_FILE} not found! Exiting..."
  exit 1
fi

# Step 3: Completion Message
echo "Middleware service built and pushed successfully!"
