# Wallet Service Helm Chart

This Helm chart deploys the **wallet-service** microservice for the One-Meristem application on a Kubernetes cluster. The wallet-service is responsible for managing users’ wallets, transaction accounts, and payment gateway integrations (e.g., Providus, WEMA).

---

## 📦 Features

- Deploys the `wallet-service` container with Kubernetes best practices.
- Injects environment variables and secrets securely using Huawei Cloud CSMS and native Kubernetes secrets.
- Mounts persistent storage via EVS PVC.
- Supports dynamic configuration via `values.yaml`.

---

## 🧾 Prerequisites

- Helm 3+
- Kubernetes 1.19+ (Huawei Cloud CCE recommended)
- Huawei Cloud OBS, CSMS, and ELB configured
- Namespace and service account provisioned

---

## 🚀 Deployment

```bash
# To test/valiadte with helm
helm dependency build #Helm will copy the shared-resources chart from the specified path into wallet-service/charts/

helm template wallet-service .

helm lint .

# Deploy the chart
helm install wallet-service . \
  --namespace one-meristem-app \
  --values values.yaml

🔐 Security
Secrets like passwords, encryption keys, and Redis credentials are injected using secretEnv.

Huawei CSMS is used for external secret management via CSI driver.

🛠️ Maintainers
Meristem Innovation Team
Esosa Mumen
https://github.com/Meristem-Innovation-Team
