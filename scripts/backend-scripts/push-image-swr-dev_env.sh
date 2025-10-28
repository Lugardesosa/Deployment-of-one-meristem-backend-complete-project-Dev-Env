#!/bin/bash
set -e

echo "=================================================="
echo "STARTING PUSH SCRIPT (Dry-run mode: no git push)"
echo "=================================================="

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

echo "--------------------------------------------------"
echo "DEBUG: Raw content of build_output/changed_services.txt"
cat build_output/changed_services.txt
echo "--------------------------------------------------"
echo "DEBUG: Parsed services:"
printf '  - %s\n' "${SERVICES_ARRAY[@]}"
echo "Using IMAGE_TAG=${IMAGE_TAG}"
echo "=================================================="

# ----------------------------------------
# Authenticate with Huawei Cloud SWR
# ----------------------------------------
echo "Logging into Huawei SWR..."
echo "${HUAWEI_SWR_PASSWORD}" | docker login \
  -u "${SWR_REGION}@${HUAWEI_SWR_USERNAME}" \
  --password-stdin "${SWR_REGISTRY_URL}"

if [ $? -ne 0 ]; then
  echo "Huawei SWR login failed. Check your credentials or token expiration."
  exit 1
else
  echo "Successfully logged into Huawei SWR."
fi

# ensure a single place for storing pushed image info (clean each run)
rm -rf image_output || true
mkdir -p image_output
echo "dircetory image_output/ created for pushed image and tags pushed to SWR"
# files we will maintain:
# image_output/image_tag.txt      -> last IMAGE_TAG used (single scalar)
# image_output/built_images.txt   -> newline-separated list of pushed image full refs
# image_output/image_map.csv      -> CSV: service,full_image_ref (handy for lookups)
> image_output/built_images.txt
> image_output/image_map.csv
#Explicitly stores the image tag used for reference
echo "${IMAGE_TAG}" > image_output/image_tag.txt 
echo "DEBUG: image_output initialized (will be overwritten each run)"

# ----------------------------------------
# Loop over array and push images
# ----------------------------------------
for SERVICE in "${SERVICES_ARRAY[@]}"; do
  echo "--------------------------------------------------"
  echo "Processing service: ${SERVICE}"

  if ! docker image inspect "${SERVICE}:${IMAGE_TAG}" > /dev/null 2>&1; then
    echo "Image ${SERVICE}:${IMAGE_TAG} not found locally — skipping."
    continue
  fi

  echo "Tagging image ${SERVICE}:${IMAGE_TAG}..."
  docker tag "${SERVICE}:${IMAGE_TAG}" "${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SERVICE}:${IMAGE_TAG}"

  echo "Pushing image ${SERVICE}:${IMAGE_TAG} to SWR..."
  docker push "${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SERVICE}:${IMAGE_TAG}"

  # Output export block for tracking pushed images**
  FULL_IMAGE_REF="${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SERVICE}:${IMAGE_TAG}"
  echo "${FULL_IMAGE_REF}" >> image_output/built_images.txt
  echo "${SERVICE},${FULL_IMAGE_REF}" >> image_output/image_map.csv

  # ----------------------------------------
  # Update Helm values
  # ----------------------------------------
  VALUES_FILE="${HELM_MOBILE_REPO_PATH}/${SERVICE}/values-${BRANCH_ENV}.yaml"
  if [[ -f "$VALUES_FILE" ]]; then
      echo "Found values file: $VALUES_FILE"
      
      echo "--------------------------------------------------"
      echo "DEBUG: Files in ${HELM_MOBILE_REPO_PATH}/${SERVICE}:"
      ls -la "${HELM_MOBILE_REPO_PATH}/${SERVICE}/"
      
      echo "DEBUG: Before update — current image config in ${VALUES_FILE}:"
      yq e '.image' "$VALUES_FILE"
      echo "--------------------------------------------------"

      echo "Updating Helm image.repository and image.tag for ${SERVICE}..."
      yq e -i ".image.repository = \"${SWR_REGISTRY_URL}/${SWR_ORGANIZATION_NAME}/${SERVICE}\"" "$VALUES_FILE"
      yq e -i ".image.tag = \"${IMAGE_TAG}\"" "$VALUES_FILE"

      echo "Updated image fields successfully!"
      echo "DEBUG: After update — new image config:"
      yq e '.image' "$VALUES_FILE"

  else
      echo "Values file ${VALUES_FILE} not found. Skipping Helm substitution."
      echo "DEBUG: Available files in ${HELM_MOBILE_REPO_PATH}/${SERVICE}:"
      ls -la "${HELM_MOBILE_REPO_PATH}/${SERVICE}/" || echo "Directory not found"
  fi
done

# ----------------------------------------
# Simulate commit (Dry Run)
# ----------------------------------------
echo "=================================================="
echo "GIT DEBUG SECTION (No push or commit performed)"
echo "=================================================="

git config user.name "github-actions[bot]"
git config user.email "github-actions[bot]@users.noreply.github.com"

echo "DEBUG: Current branch info:"
git status

echo "DEBUG: Staging relevant directories (dry-run)"
for DIR in charts argocd scripts backend; do
  if [ -d "$DIR" ]; then
    echo "Would stage directory: $DIR"
    git add -n "$DIR" || true  # -n means dry-run add
  else
    echo "Directory not found (skipping): $DIR"
  fi
done

# Skip build_output
echo "Ensuring build_output/ is excluded..."
git status --ignored | grep "build_output" || echo "build_output not staged."

echo "=================================================="
echo "DEBUG COMPLETE — Substitutions verified."
echo "No code has been committed or pushed in this run."
echo "=================================================="
