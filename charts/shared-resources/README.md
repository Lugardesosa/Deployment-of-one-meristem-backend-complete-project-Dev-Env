# shared-resources Helm Chart

This Helm chart provisions all the shared Kubernetes resources required by the One Meristem mobile backend application. These include:

- **PersistentVolume**
- **ServiceAccount**
- **RBAC (Role and RoleBinding)**
- **Kubernetes Secret**
- **SecretProviderClass** (for Huawei Cloud CSMS integration)

  ## 📁 Chart Structure

shared-resources/
├── Chart.yaml
├── values.yaml
├── README.md
└── templates/
├── persistentvolume.yaml
├── role.yaml
├── rolebinding.yaml
├── serviceaccount.yaml
├── secret.yaml
└── secretproviderclass.yaml

---

### creat a Kubernetes imagePullSecret using a declarative YAML file inside your shared-resources

## First Generate it manually with this

# Encode it first
kubectl create secret docker-registry huawei-swr-auth \
  --docker-server=swr.af-south-1.myhuaweicloud.com \
  --docker-username=af-south-1@HUAWEI_SWR_USERNAME \
  --docker-password=HUAWEI_SWR_PASSWORD \
  --docker-email=talk2lugard@gmail.com \
  --dry-run=client -o jsonpath='{.data.\.dockerconfigjson}' | base64 -d

# Then Re-Encode for Helm
kubectl create secret docker-registry huawei-swr-auth \
  --docker-server=swr.af-south-1.myhuaweicloud.com \
  --docker-username=af-south-1@HUAWEI_SWR_USERNAME \
  --docker-password=HUAWEI_SWR_PASSWORD \
  --docker-email=talk2lugard@gmail.com \
  --dry-run=client -o yaml | grep dockerconfigjson | awk '{print $2}'

---

## 🔧 Installation

> Make sure the `one-meristem-app` namespace already exists.

To install the chart:

```bash
helm install shared-resources ./shared-resources --namespace one-meristem-app

To upgrade:

bash
helm upgrade shared-resources ./shared-resources --namespace one-meristem-app
To uninstall:

bash
helm uninstall shared-resources --namespace one-meristem-app




