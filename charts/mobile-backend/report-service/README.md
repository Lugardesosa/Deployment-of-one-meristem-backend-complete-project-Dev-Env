# report-service Helm Chart

This Helm chart deploys the **Report Service** for the One-Meristem mobile investment platform. The Report Service is responsible for generating and managing financial transaction histories, user reports, and analytics summaries across the platform.

---

## Features

- Pulls container image from Huawei SWR registry.
- Mounts Huawei Cloud CSMS credentials via CSI driver.
- Reads secure secrets for Kafka, Redis, Database, and mailing.
- Supports persistent storage (Huawei EVS volume).
- Exposes internal service via ClusterIP.
- Supports environment-specific Git branch/profile via Helm values.
- Integrated with `shared-resources` chart for shared volumes, roles, and secrets.

---

## Prerequisites

- Kubernetes 1.20+
- Helm 3.0+
- Persistent Volume Claim (PVC) provisioned
- CSI Driver for Huawei Cloud CSMS
- `shared-resources` Helm chart deployed (for common ServiceAccount, PVC, Secrets)

---

## 🚀 Deployment

```bash
# To test/valiadte with helm
helm dependency build #Helm will copy the shared-resources chart from the specified path into notification-service/charts/

helm template report-service .

helm lint .

# Deploy the chart
helm install report-service . \
  --namespace one-meristem-app \
  --values values.yaml

🔐 Security
Secrets like passwords, encryption keys, and Redis credentials are injected using secretEnv.

Huawei CSMS is used for external secret management via CSI driver.

🛠️ Maintainers
Meristem Innovation Team
Esosa Mumen
https://github.com/Meristem-Innovation-Team
