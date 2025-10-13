# Cloud Gateway Helm Chart

This Helm chart deploys the `cloud-gateway` microservice for the **One-Meristem** investment application on a Kubernetes cluster managed via **Huawei Cloud CCE**. The Cloud Gateway acts as the main entry point and reverse proxy for all external requests targeting internal backend microservices.

---

## 📦 Features

- Deploys a Spring Boot-based gateway service
- Exposes the service via Huawei Cloud Ingress ELB
- Supports dynamic environment variables and secrets via Kubernetes secrets and Huawei CSMS
- Integrates with Persistent Volumes for secure file storage
- Configurable Horizontal Pod Autoscaler (HPA)
- TLS termination and Brotli compression via Ingress annotations

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
helm dependency build #Helm will copy the shared-resources chart from the specified path into cloud-gateway/charts/

helm template cloud-gateway .

helm lint .

# Deploy the chart
helm install cloud-gateway . \
  --namespace one-meristem-app \
  --values values.yaml

🔐 Security
Secrets like passwords, encryption keys, and Redis credentials are injected using secretEnv.

Huawei CSMS is used for external secret management via CSI driver.

🛠️ Maintainers
Meristem Innovation Team
Esosa Mumen
https://github.com/Meristem-Innovation-Team

