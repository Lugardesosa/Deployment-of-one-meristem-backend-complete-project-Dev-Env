#!/bin/bash

# --- Configurable Variables ---
NAMESPACE="argocd"
ARGOCD_SECRET_NAME="argocd-initial-admin-secret"
ARGOCD_HOST="argocd.internal.local"  # Replace with your actual domain or use port-forward below
ARGOCD_USERNAME="admin"

# --- Step 1: Fetch Argo CD admin password ---
echo "Fetching Argo CD admin password from Kubernetes secret..."
ARGOCD_PASSWORD=$(kubectl -n $NAMESPACE get secret $ARGOCD_SECRET_NAME -o jsonpath="{.data.password}" | base64 -d)

if [[ -z "$ARGOCD_PASSWORD" ]]; then
  echo "Failed to retrieve Argo CD password. Ensure Argo CD is fully deployed and the secret exists."
  exit 1
fi

echo "Argo CD admin password retrieved."

# --- Step 2: Argo CD CLI Login ---
echo "Logging into Argo CD at $ARGOCD_HOST..."
argocd login $ARGOCD_HOST --username $ARGOCD_USERNAME --password $ARGOCD_PASSWORD --insecure

if [[ $? -ne 0 ]]; then
  echo "Argo CD login failed. Check if the domain is reachable or if port-forwarding is needed."
  exit 1
fi

echo "Logged into Argo CD successfully."

# --- Optional: Port-forward if no LoadBalancer or Ingress ---
#Prompt the user for a response Yes or No
read -p "Do you want to port-forward Argo CD UI to http://localhost:8080? [y/N] " PFWD
if [[ "$PFWD" == "y" || "$PFWD" == "Y" ]]; then
  echo "Port-forwarding Argo CD UI to http://localhost:8080"
  kubectl -n $NAMESPACE port-forward svc/argocd-server 8080:443
fi

# --- Optional: Print Access Info ---
echo ""
echo " Argo CD setup complete!"
echo " Access Argo CD UI at: https://$ARGOCD_HOST"
echo " Username: $ARGOCD_USERNAME"
echo " Password: $ARGOCD_PASSWORD"
