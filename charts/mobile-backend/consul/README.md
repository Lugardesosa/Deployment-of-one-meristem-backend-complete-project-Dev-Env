# Consul Microservice Helm Chart

This Helm chart deploys the [Consul](https://www.consul.io/) microservice for the **One-Meristem** platform onto a Kubernetes cluster. Consul is used as a service discovery and configuration coordination system across microservices.

---

## 📦 Chart Details

- **Chart Name:** consul
- **App Version:** 1.21.2
- **Chart Version:** 0.1.0
- **Type:** application

---

## 🚀 Features

- Deploys a single Consul agent in server mode.
- Exposes the HTTP UI (`8500`) via NodePort for ELB routing.
- Persists state using EVS (Everest Volume Storage).
- Configurable CPU/Memory requests and limits.
- Easily integrates with a shared `PersistentVolume`.

---

## 📂 File Structure

```text
.
├── Chart.yaml
├── values.yaml
├── templates/
│   ├── deployment.yaml
│   ├── service.yaml
    └── ingress.yaml
    └── horizontalPodAutoscaler.yaml
└── README.md


---

Template Rendering
helm template consul . --namespace one-meristem-app

Linting
helm lint .

Dry-run Install
helm install consul . --namespace one-meristem-app --dry-run --debug

Accessing the UI
The Consul UI is exposed on port 8500 through a NodePort service. You can route traffic to it via a Huawei Cloud ELB using the same public IP and hostname used for your main ingress.

 Notes
Make sure the ELB has a security rule to allow TCP traffic on port 8500.
This chart assumes your cluster already has the PVC (consul-pvc-evs) created or provisioned via a shared-resources Helm chart.

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
  