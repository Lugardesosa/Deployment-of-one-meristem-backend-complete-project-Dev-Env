# Notification Service – Helm Chart

This Helm chart deploys the **Notification Service** for the One Meristem application. The service is responsible for sending user notifications via email, SMS, and in-app channels. It integrates with external mailers, Redis, Kafka, and CreditSwitch APIs.

---

## Features

- Containerized Spring Boot microservice
- CSI volume mount from Huawei Cloud CSMS (for secrets)
- Environment variable-based configuration
- Persistent volume for local data storage
- Integration with Redis, Kafka, Config Server
- Secure secret injection for credentials

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

helm template notification-service .

helm lint .

# Deploy the chart
helm install notification-service . \
  --namespace one-meristem-app \
  --values values.yaml

🔐 Security
Secrets like passwords, encryption keys, and Redis credentials are injected using secretEnv.

Huawei CSMS is used for external secret management via CSI driver.

🛠️ Maintainers
Meristem Innovation Team
Esosa Mumen
https://github.com/Meristem-Innovation-Team
