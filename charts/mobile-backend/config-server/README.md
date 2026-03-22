# Config Server Helm Chart

This Helm chart deploys the **Config Server** microservice for the **One-Meristem** application. The Config Server acts as a centralized configuration hub for all Spring Boot microservices by connecting to a remote Git repository.

---

## 📦 Features

- Spring Cloud Config Server deployment
- Secure connection to private GitHub repo using Kubernetes secrets
- Centralized configuration management for all backend services
- Mounted secrets from Kubernetes for secure Git credentials and encryption
- Persistent volume support for local storage and caching
- Resource and replica configuration for production-grade performance

---

## 🧾 Prerequisites

- Helm 3+
- Kubernetes 1.19+ (Huawei Cloud CCE recommended)
- Huawei Cloud CSMS and OBS configured
- Namespace and `ServiceAccount` created (`app-spc-sa`)
- K8s secret named `credentials` pre-provisioned

---

## 🚀 Deployment

```bash
# To test/valiadte with helm
helm dependency build #Helm will copy the shared-resources chart from the specified path into cloud-gateway/charts/

helm template notification-service .

helm lint .


helm install config-server . \
  --namespace one-meristem-app \
  --values values.yaml


Environment Variables
The following Spring environment variables are configurable:

SPRING_PROFILES_ACTIVE

SPRING_CLOUD_CONFIG_SERVER_GIT_CLONE_ON_START

SPRING_CLOUD_CONFIG_SERVER_GIT_TIMEOUT

SPRING_CLOUD_CONFIG_SERVER_GIT_REFRESH_RATE

ENCRYPTION_KEY

H2_PASSWORD


🔐 Secrets
This chart expects the following keys to be present in the Kubernetes Secret named credentials:

Key  Purpose
key1 GitHub Username
key2 GitHub Personal Access Token
key3 Admin password
key4 Encryption key
key13 H2 database password

🛠️ Maintainers
Meristem Innovation Team
Esosa Mumen
https://github.com/Meristem-Innovation-Team

