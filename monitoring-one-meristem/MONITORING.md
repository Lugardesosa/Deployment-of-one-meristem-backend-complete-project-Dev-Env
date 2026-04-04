# One Meristem — Monitoring Stack Documentation

## Overview

This document covers the full observability stack set up for the One Meristem backend application running on a Huawei Cloud ECS server using Docker Compose. It explains what each component does, how they connect to each other, and how to deploy the full stack from scratch.

---

## Stack Components

The monitoring stack consists of 8 containers working together:

| Component | Image | Purpose |
|-----------|-------|---------|
| **Prometheus** | `prom/prometheus:latest` | Metrics collection and storage |
| **Grafana** | `grafana/grafana:latest` | Visualization and dashboards |
| **Loki** | `grafana/loki:latest` | Log aggregation and storage |
| **Promtail** | `grafana/promtail:2.6.1` | Log shipper — reads Docker logs and sends to Loki |
| **Tempo** | `grafana/tempo:2.6.1` | Distributed tracing storage |
| **OTel Collector** | `otel/opentelemetry-collector-contrib:latest` | Central telemetry router |
| **Node Exporter** | `prom/node-exporter:latest` | Host system metrics (CPU, RAM, disk) |
| **cAdvisor** | `gcr.io/cadvisor/cadvisor:latest` | Per-container resource metrics |

---

## How They Connect

```
┌─────────────────────────────────────────────────────────────────┐
│                        DATA SOURCES                              │
│                                                                  │
│  Spring Boot Services ──► /actuator/prometheus ──► Prometheus   │
│  Host System ───────────► node-exporter ──────────► Prometheus   │
│  Docker Containers ─────► cadvisor ───────────────► Prometheus   │
│                                                                  │
│  All Docker Containers ─► Promtail ───────────────► Loki        │
│                                                                  │
│  Spring Boot Services ──► OTel Collector ─┬────────► Prometheus  │
│                                            ├────────► Loki        │
│                                            └────────► Tempo       │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
                           Grafana UI
                    (Dashboards + Explore)
```

### Connection Details

**Metrics flow:**
- `node-exporter` exposes host metrics on port `9100` → Prometheus scrapes every 15 seconds
- `cadvisor` exposes container metrics on port `8080` → Prometheus scrapes every 15 seconds
- Each Spring Boot service exposes metrics at `/{context-path}/actuator/prometheus` → Prometheus scrapes every 15 seconds
- Prometheus stores all metrics for 30 days on the data disk at `/mnt/data/monitoring/prometheus`

**Logs flow:**
- `Promtail` reads Docker container log files directly from `/mnt/data/docker/containers/`
- It uses Docker service discovery via the Docker socket to automatically label each log line with the container name
- Labelled logs are pushed to Loki at `http://loki:3100/loki/api/v1/push`
- Loki stores logs for 30 days at `/mnt/data/monitoring/loki`

**Tracing flow (future — requires backend changes):**
- Spring Boot services send traces via OTel Java agent to `otel-collector:4317`
- OTel Collector routes traces to Tempo at `tempo:4317`
- OTel Collector routes logs to Loki at `loki:3100/otlp`
- OTel Collector exposes metrics to Prometheus at port `8889`

**Grafana:**
- Connects to Prometheus at `http://prometheus:9090`
- Connects to Loki at `http://loki:3100`
- Connects to Tempo at `http://tempo:3200`
- All three are provisioned as datasources automatically

---

## Directory Structure

All monitoring config files live inside the repo under `monitoring-one-meristem/`:

```
monitoring-one-meristem/
├── prometheus.yml                          # Prometheus scrape config
├── loki/
│   └── loki-config.yml                     # Loki storage and retention config
├── tempo/
│   └── tempo-config.yml                    # Tempo tracing config
├── otel-collector/
│   └── otel-collector-config.yml           # OTel Collector pipeline config
├── promtail/
│   └── promtail-config.yml                 # Promtail Docker log scraping config
└── grafana/
    └── provisioning/
        ├── datasources/
        │   └── datasources.yml             # Auto-provisioned datasources
        └── dashboards/
            └── dashboards.yml              # Dashboard provisioning config
```

All runtime data (metrics, logs, traces) is persisted on the data disk at `/mnt/data/monitoring/` and is **not** inside the repo.

---

## Prometheus Scrape Configuration

Each Spring Boot service has its own context path. The actuator endpoint follows the pattern `/{context-path}/actuator/prometheus`:

| Service | Port | Metrics Path |
|---------|------|--------------|
| cloud-gateway | 20010 | `/actuator/prometheus` |
| users-service | 20020 | `/api/users/actuator/prometheus` |
| notification-service | 20030 | `/api/notification/actuator/prometheus` |
| wallet-service | 20040 | `/api/wallets/actuator/prometheus` |
| report-service | 20050 | `/api/reports/actuator/prometheus` |
| trustees-service | 20060 | `/api/trustees/actuator/prometheus` |
| wealth-service | 20070 | `/api/wealths/actuator/prometheus` |
| config-server | 20000 | `/actuator/prometheus` |

> **Important:** For Prometheus scraping to work, the backend config at `backend/meriapp-config/application-dev.yaml` must have `prometheus: access: read_only` (not `access: none`). This is a one-line change the dev team needs to make.

---

## Data Persistence

All monitoring data is stored on the dedicated data disk (`/dev/vdb`, mounted at `/mnt/data`) to avoid filling the root disk:

| Data | Host Path | Container Path |
|------|-----------|----------------|
| Prometheus metrics | `/mnt/data/monitoring/prometheus` | `/prometheus` |
| Loki logs | `/mnt/data/monitoring/loki` | `/tmp/loki` |
| Tempo traces | `/mnt/data/monitoring/tempo` | `/tmp/tempo` |
| Grafana dashboards/settings | `/mnt/data/monitoring/grafana` | `/var/lib/grafana` |

---

## Deployment Guide

### Prerequisites

- Docker and Docker Compose installed on the server
- Data disk mounted at `/mnt/data` (see disk setup docs)
- All monitoring config files present in the repo under `monitoring-one-meristem/`

### Step 1 — Create persistent directories on the data disk

```bash
mkdir -p /mnt/data/monitoring/prometheus
mkdir -p /mnt/data/monitoring/grafana
mkdir -p /mnt/data/monitoring/loki
mkdir -p /mnt/data/monitoring/tempo

# Set correct ownership
chown -R 65534:65534 /mnt/data/monitoring/prometheus
chown -R 65534:65534 /mnt/data/monitoring/loki
chown -R 65534:65534 /mnt/data/monitoring/tempo
chown -R 472:472 /mnt/data/monitoring/grafana

# Set permissions for loki and tempo
chmod -R 777 /mnt/data/monitoring/loki
chmod -R 777 /mnt/data/monitoring/tempo
```

### Step 2 — Ensure monitoring configs are in place on the server

The docker-compose.yml mounts configs from `./monitoring/` on the server. If deploying fresh, copy the configs from the repo:

```bash
cp -r monitoring-one-meristem/ /root/one_meristem_app/monitoring
```

### Step 3 — Add GRAFANA_PASSWORD to .env

```bash
echo "GRAFANA_PASSWORD=YourStrongPasswordHere" >> /root/one_meristem_app/.env
```

### Step 4 — Start the monitoring stack

```bash
cd /root/one_meristem_app
sudo docker-compose -f docker-compose.yml up -d \
  node-exporter cadvisor loki tempo otel-collector prometheus grafana promtail
```

### Step 5 — Verify all containers are running

```bash
docker ps | grep -E "node-exporter|cadvisor|loki|tempo|otel|prometheus|grafana|promtail"
```

All 8 should show `Up`.

### Step 6 — Verify logs are flowing into Loki

```bash
# Check Loki has container name labels
docker exec prometheus wget -qO- "http://loki:3100/loki/api/v1/label/container_name/values" 2>&1
```

You should see all container names listed.

### Step 7 — Verify Prometheus targets

```bash
docker exec prometheus wget -qO- http://localhost:9090/api/v1/targets | \
  python3 -m json.tool | grep -E '"health"|"job"'
```

`node-exporter`, `cadvisor` and `prometheus` should show `"health": "up"`. Spring Boot services will show `"health": "down"` until the backend team enables the prometheus actuator endpoint.

### Step 8 — Access Grafana

Grafana is exposed on port 3000. Access it at:

```
http://YOUR_SERVER_IP:3000
```

> **Note:** Port 3000 must be open in the Huawei Cloud security group, restricted to your team's IP addresses only.

Default credentials:
```
Username: admin
Password: value of GRAFANA_PASSWORD in .env
```

---

## Adding Datasources in Grafana

Datasources are auto-provisioned from `grafana/provisioning/datasources/datasources.yml`. If they don't appear automatically (due to Grafana version differences), add them manually:

1. Go to **Connections → Data sources → Add new data source**

**Prometheus:**
- Type: `Prometheus`
- URL: `http://prometheus:9090`
- Set as default: Yes

**Loki:**
- Type: `Loki`
- URL: `http://loki:3100`

**Tempo:**
- Type: `Tempo`
- URL: `http://tempo:3200`

Click **Save & test** on each — all should show green.

---

## Dashboards

Import these pre-built dashboards from Grafana's public library:

### 1. Server Health (Node Exporter Full)
- **Grafana ID:** `1860`
- **Datasource:** Prometheus
- **Shows:** Host CPU usage, memory usage, disk I/O, network traffic, system load

**How to import:**
1. Dashboards → New → Import
2. Enter ID `1860` → Load
3. Select Prometheus datasource → Import

### 2. Container Health (Docker Monitoring)
- **Grafana ID:** `15798`
- **Datasource:** Prometheus
- **Shows:** Running containers count, per-container CPU, per-container memory usage

**How to import:**
1. Dashboards → New → Import
2. Enter ID `15798` → Load
3. Select Prometheus datasource → Import

---

## Viewing Logs (Loki Explore)

Your dev team can search logs for any service using Grafana's Explore feature:

1. Click the **compass icon (Explore)** in the left sidebar
2. Select **Loki** from the datasource dropdown at the top
3. Use the query box to filter logs

### Common Queries

**View all logs for a specific service:**
```
{container_name="users-service"}
```

**Filter by log level:**
```
{container_name="users-service"} |= "ERROR"
{container_name="users-service"} |= "WARN"
```

**Search for a specific keyword:**
```
{container_name="cloud-gateway"} |= "403"
{container_name="users-service"} |= "Exception"
```

**View logs from multiple services:**
```
{container_name=~"users-service|cloud-gateway|wallet-service"}
```

**Available container names:**
```
users-service, cloud-gateway, notification-service, wallet-service,
report-service, trustees-service, wealth-service, config-server,
postgres, redis, kafka, consul, nginx, nginx-acme
```

---

## Enabling Spring Boot Metrics (Backend Team Action Required)

For Prometheus to scrape metrics from Spring Boot services, the backend team needs to make one change in `backend/meriapp-config/application-dev.yaml`:

**Change from:**
```yaml
    prometheus:
      access: none
```

**Change to:**
```yaml
    prometheus:
      access: read_only
```

After pushing this change and restarting services, Prometheus will automatically start collecting JVM metrics, HTTP request rates, error rates, and response times from all services.

---

## Creating Grafana Users for the Dev Team

1. Go to **☰ → Administration → Users and access → Users**
2. Click **New user**
3. Fill in name, email, username and password
4. Set role to **Viewer** — they can explore and view but cannot edit dashboards

Share these details with the dev team:
```
URL: http://YOUR_SERVER_IP:3000
Username: devteam
Password: [password you set]
```

---

## Troubleshooting

### Loki permission denied on startup
```bash
chmod -R 777 /mnt/data/monitoring/loki
sudo docker-compose restart loki
```

### Tempo fails to start with Kafka error
Tempo v2.7+ changed default ingestion to Kafka. Always use `grafana/tempo:2.6.1` — do not use `latest`.

### Prometheus targets showing 404 for Spring Boot services
The prometheus actuator endpoint is disabled. Backend team needs to set `prometheus: access: read_only` in `application-dev.yaml`.

### Grafana datasources not auto-provisioned
Add them manually via **Connections → Data sources**. This is a known issue with newer Grafana versions and the legacy provisioning format.

### Container logs not appearing in Loki
Check Promtail is running and can reach the Docker socket:
```bash
docker logs promtail --tail=20
docker exec prometheus wget -qO- "http://loki:3100/loki/api/v1/labels" 2>&1
```

---

## Resource Usage

At idle, the monitoring stack consumes approximately:

| Container | RAM |
|-----------|-----|
| Prometheus | ~150MB |
| Grafana | ~100MB |
| Loki | ~80MB |
| Tempo | ~50MB |
| OTel Collector | ~50MB |
| Promtail | ~30MB |
| Node Exporter | ~20MB |
| cAdvisor | ~50MB |
| **Total** | **~530MB** |

The server has 15GB RAM with ~9GB available — the monitoring stack fits comfortably.