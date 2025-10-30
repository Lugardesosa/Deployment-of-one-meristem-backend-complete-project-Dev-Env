# One Meristem App – Kubernetes Helm Deployment Guide

This guide covers the full installation, upgrade, uninstallation, and secret integration process for the One Meristem App, a microservices-based investment platform deployed to Huawei Cloud using Helm and Kubernetes.

---

## 🚀 Full Installation Process

### 1. Create Namespace (if not already created)

```bash
kubectl create namespace one-meristem-app
```

### 2. Install Shared Resources First

These include Kubernetes secrets (mounted from Huawei CSMS using DEW), service accounts, roles, bindings, and persistent volumes.

```bash
helm install shared-resources ./applications/charts/shared-resources -n one-meristem-app
```

### 3. Install Microservices in Any Order (Recommended Order Below)

```bash
helm install config-server ./applications/charts/config-server -n one-meristem-app
helm install consul ./applications/charts/consul -n one-meristem-app
helm install users-service ./applications/charts/users-service -n one-meristem-app
helm install report-service ./applications/charts/report-service -n one-meristem-app
helm install wallet-service ./applications/charts/wallet-service -n one-meristem-app
helm install notification-service ./applications/charts/notification-service -n one-meristem-app
helm install cloud-gateway ./applications/charts/cloud-gateway -n one-meristem-app
```

If any service uses a custom `values.yaml`, add the `-f` flag:

```bash
helm install config-server ./applications/charts/config-server -n one-meristem-app -f ./applications/charts/config-server/values.yaml
```

---

## 🔁 Uninstall All Helm Releases

To remove all installed resources:

```bash
helm uninstall cloud-gateway -n one-meristem-app
helm uninstall notification-service -n one-meristem-app
helm uninstall wallet-service -n one-meristem-app
helm uninstall report-service -n one-meristem-app
helm uninstall users-service -n one-meristem-app
helm uninstall consul -n one-meristem-app
helm uninstall config-server -n one-meristem-app
helm uninstall shared-resources -n one-meristem-app
```

Optional: delete namespace entirely (use with caution):

```bash
kubectl delete namespace one-meristem-app
```

---

## 🔐 Secrets from Huawei Cloud CSMS (DEW)

Each microservice requiring credentials (PostgreSQL, Redis, Kafka, GitHub tokens) references secrets mounted securely via DEW:

1. Secrets are stored in **Huawei Cloud CSMS**.
2. `shared-resources` deploys:
   - `SecretProviderClass` definitions
   - `Volumes` using the DEW CSI Driver
   - Kubernetes `Secrets` from `volumes.secretObjects`
3. Microservices reference secrets via:
   - Mounted volumes (`/mnt/secrets`)
   - Env vars templated in `values.yaml` or `deployment.yaml`

Ensure the secrets exist in CSMS before installing.

---

## 🧪 Troubleshooting

Check pod logs:

```bash
kubectl logs -n one-meristem-app <pod-name>
```

Check pod events and resource issues:

```bash
kubectl describe pod -n one-meristem-app <pod-name>
```

Common issues:

- `CrashLoopBackOff`: Missing or misconfigured secrets or Git access
- `ImagePullBackOff`: Image tag not found or registry auth issue
- `Init container failed`: Secret mount issue (DEW or Git credentials)

---

## 📂 File Structure

```bash
applications/
├── charts/
│   ├── shared-resources/
│   ├── config-server/
│   ├── consul/
│   ├── users-service/
│   ├── report-service/
│   ├── wallet-service/
│   ├── notification-service/
│   └── cloud-gateway/
```

Each chart includes:

- `Chart.yaml`
- `values.yaml`
- `templates/` folder:
  - `deployment.yaml`
  - `service.yaml`
  - optional `ingress.yaml`, `hpa.yaml`

---

## 🔁 Upgrade Services

To apply changes to a chart after modifying values or templates:

```bash
helm upgrade users-service ./applications/charts/users-service -n one-meristem-app -f ./applications/charts/users-service/values.yaml
```

You can also rollback to a previous release:

```bash
helm rollback users-service 1 -n one-meristem-app
```

---

## 🧼 Full Clean-Up

To remove all workloads (danger zone):

```bash
kubectl delete all --all -n one-meristem-app
```

---

## ✅ Final Checklist Before Production

- [ ] Namespace `one-meristem-app` created
- [ ] Huawei CSMS secrets exist and mounted via `SecretProviderClass`
- [ ] Shared resources (volumes, roles, secrets, service accounts) installed
- [ ] All microservices installed via Helm
- [ ] All pods are in `Running` state
- [ ] GitHub config repo (for config-server) is accessible
- [ ] Ingress or ELB routes requests correctly to cloud-gateway

---

✅ Final Checklist Before Production
 Namespace one-meristem-app created

 Huawei CSMS secrets exist and mounted via SecretProviderClass

 Shared resources (volumes, roles, secrets, service accounts, role-binding) installed

 All microservices installed via Helm

 All pods are in Running state

 GitHub config repo (for microservice) is accessible

 Ingress or ELB routes requests correctly to cloud-gateway

---
> 🛡️ Meristem Innovation Team | Cloud-Native Investment App – Powered by Huawei Cloud CCE, CSMS, Helm, and Kubernetes.
