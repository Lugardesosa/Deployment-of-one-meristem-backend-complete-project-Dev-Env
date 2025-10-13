# Users Service Helm Chart

This Helm chart deploys the `users-service` microservice to a Kubernetes cluster on Huawei Cloud CCE. The `users-service` is responsible for user authentication, onboarding, document verification (Smile ID integration), and profile management in the One-Meristem application.

---

## Features

- Containerized Spring Boot microservice
- Integrated with Huawei Cloud OBS, Redis, Kafka, and Config Server
- Uses Secrets Store CSI for dynamic secret injection from Huawei CSMS
- Persistent storage via EVS-backed PVC
- Compatible with GitOps workflows and Helm-based CI/CD pipelines

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
helm dependency build #Helm will copy the shared-resources chart from the specified path into users-service/charts/

helm template users-service .

helm lint .

# Deploy the chart
helm install users-service . \
  --namespace one-meristem-app \
  --values values.yaml

🔐 Security
Secrets like passwords, encryption keys, and Redis credentials are injected using secretEnv.

Huawei CSMS is used for external secret management via CSI driver.

🛠️ Maintainers
Meristem Innovation Team
Esosa Mumen
https://github.com/Meristem-Innovation-Team
