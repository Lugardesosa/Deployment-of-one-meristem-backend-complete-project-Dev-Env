# Deploying a Financial App Using CI/CD Pipeline (GitHub Actions) on a Huawei Cloud ECS with Monitoring and Logs Enabled (Prometheus & Grafana, Loki)

A production-ready deployment guide for a **Java Spring Boot microservices** investment platform targeting retail investors. This repository covers the full infrastructure setup — from provisioning a Huawei Cloud ECS server, configuring a self-hosted GitHub Actions runner, setting up CI/CD pipelines, to enabling full observability with Prometheus, Grafana and Loki.

---

## Table of Contents

1. [Architecture Overview](#architecture-overview)
2. [Technology Stack](#technology-stack)
3. [Repository Structure](#repository-structure)
4. [Infrastructure Requirements](#infrastructure-requirements)
5. [Server Provisioning](#server-provisioning)
6. [GitHub Actions Runner Setup](#github-actions-runner-setup)
7. [Security Group Configuration](#security-group-configuration)
8. [Environment Variables](#environment-variables)
9. [Data Disk Setup (Optional but Recommended)](#data-disk-setup-optional-but-recommended)
10. [Server Directory Setup](#server-directory-setup)
11. [PostgreSQL Initialisation](#postgresql-initialisation)
12. [NGINX and SSL Configuration](#nginx-and-ssl-configuration)
13. [CI Pipeline](#ci-pipeline)
14. [CD Pipeline](#cd-pipeline)
15. [Microservices](#microservices)
16. [Monitoring Stack](#monitoring-stack)
17. [Grafana Access and User Management](#grafana-access-and-user-management)
18. [Troubleshooting](#troubleshooting)

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                     CLIENT LAYER                             │
│              Mobile App  |  Web App                          │
└─────────────────────────┬───────────────────────────────────┘
                          │ HTTPS
┌─────────────────────────▼───────────────────────────────────┐
│                  HUAWEI CLOUD ECS (Backend Server)           │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  NGINX (nginx-proxy + acme-companion)                │    │
│  │  TLS Termination | SSL Auto-Renewal (ZeroSSL)        │    │
│  └──────────────────────┬──────────────────────────────┘    │
│                         │                                    │
│  ┌──────────────────────▼──────────────────────────────┐    │
│  │              Spring Cloud Gateway                    │    │
│  │         (Port 20010 — API Entry Point)               │    │
│  └──┬──────────┬────────┬────────┬──────────┬──────────┘    │
│     │          │        │        │          │                │
│  users  notification wallet  report  trustees  wealth        │
│  :20020  :20030   :20040  :20050  :20060   :20070           │
│                                                              │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────────┐  │
│  │ Postgres │  │  Redis   │  │  Kafka   │  │   Consul   │  │
│  └──────────┘  └──────────┘  └──────────┘  └────────────┘  │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │              MONITORING STACK                        │    │
│  │  Prometheus | Grafana | Loki | Tempo | Promtail      │    │
│  │  Node Exporter | cAdvisor | OTel Collector           │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│              HUAWEI CLOUD ECS (GitHub Runner Server)         │
│                                                              │
│  GitHub Actions Self-Hosted Runner                           │
│  ├── CI: Maven compile + test-compile + Checkstyle           │
│  └── CD: SSH → Backend Server → Pack Build → Deploy          │
└─────────────────────────────────────────────────────────────┘
```

---

## Technology Stack

| Layer | Technology |
|-------|------------|
| Language | Java 21 (LTS), Java 25 |
| Framework | Spring Boot, Spring Cloud Gateway, Spring Cloud Config |
| Service Discovery | Consul |
| Message Broker | Apache Kafka |
| Cache | Redis |
| Database | PostgreSQL 15 |
| Containerisation | Docker, Docker Compose |
| Image Build | Cloud Native Buildpacks (pack CLI) |
| Reverse Proxy | nginx-proxy |
| SSL | ZeroSSL via acme-companion |
| CI/CD | GitHub Actions (self-hosted runner) |
| Metrics | Prometheus, cAdvisor, Node Exporter |
| Visualisation | Grafana |
| Logs | Loki, Promtail |
| Tracing | Tempo, OpenTelemetry Collector |
| Cloud | Huawei Cloud ECS |

---

## Repository Structure

When cloned, the repository contains the following at the root level:

```
one_meristem_app/
├── .github/
│   └── workflows/
│       ├── ci.yaml                         # CI pipeline (PR checks)
│       └── cd.yaml                         # CD pipeline (deploy on push)
├── backend/
│   ├── cloud-gateway/                      # Spring Cloud Gateway
│   ├── config-server/                      # Spring Cloud Config Server
│   ├── users-service/                      # User auth, KYC, onboarding
│   ├── notification-service/               # Email, SMS, push notifications
│   ├── wallet-service/                     # Wallet and transactions
│   ├── report-service/                     # Investment reports
│   ├── trustees-service/                   # Trustee and beneficiary mgmt
│   ├── wealth-service/                     # Investment portfolio management
│   └── meriapp-config/                     # Spring Cloud Config YAML files
├── monitoring-one-meristem/                # Monitoring config files (version controlled)
│   ├── prometheus.yml
│   ├── loki/loki-config.yml
│   ├── tempo/tempo-config.yml
│   ├── otel-collector/otel-collector-config.yml
│   ├── promtail/promtail-config.yml
│   └── grafana/provisioning/
│       ├── datasources/datasources.yml
│       └── dashboards/dashboards.yml
├── postgres/
│   └── init-db.sh                          # PostgreSQL database init script
├── custom_headers.conf                     # NGINX custom headers for WebSocket
├── docker-compose.yml                      # Full stack compose file
└── .gitignore
```

> **Note:** The following directories are created on the server at runtime and are NOT in the repository:
> - `certs/` — SSL certificates managed by acme-companion
> - `acme/` — ACME challenge files
> - `html/` — NGINX web root for ACME challenges
> - `vhost/` — NGINX per-domain config overrides
> - `data/` — Runtime data for postgres, redis, kafka
> - `monitoring/` — Active monitoring configs (symlinked or copied from `monitoring-one-meristem/`)
> - `.env` — Environment secrets (never committed)

---

## Infrastructure Requirements

### Huawei Cloud ECS Instances

You need **two separate ECS instances**:

| Server | Purpose | Recommended Spec |
|--------|---------|-----------------|
| Backend Server | Runs all Docker containers | 8 vCPU / 16GB RAM / 150GB system disk + 100GB data disk |
| GitHub Runner Server | Runs CI/CD pipeline | 2 vCPU / 8GB RAM / 985GB disk (cannot be reduced after creation — provision at 200GB) |

> **Cost tip:** Provision the GitHub runner disk at 200GB from the start. Huawei Cloud does not allow disk shrinking — only expansion.

### Domain Name

You need a domain with DNS managed externally (e.g. Cloudflare). You need an A record pointing to your backend server's public IP:

```
Type: A
Name: onemeristemdev.meristemng.com   ← replace with your domain
Value: YOUR_SERVER_PUBLIC_IP
TTL:  300
```

---

## Server Provisioning

### Backend Server Setup

SSH into your backend server as root and run the following:

**Step 1 — Update system:**
```bash
apt update && apt upgrade -y
```

**Step 2 — Install Docker:**
```bash
curl -fsSL https://get.docker.com | sh
systemctl enable docker
systemctl start docker
```

**Step 3 — Install Docker Compose:**
```bash
curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" \
  -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
docker-compose --version
```

**Step 4 — Install pack CLI (for building images via Buildpacks):**
```bash
curl -sSL "https://github.com/buildpacks/pack/releases/download/v0.35.0/pack-v0.35.0-linux.tgz" \
  | tar -C /usr/local/bin/ --no-same-owner -xzv pack
pack --version
```

**Step 5 — Configure Docker log rotation (prevents disk filling over time):**
```bash
cat > /etc/docker/daemon.json << 'EOF'
{
  "data-root": "/mnt/data/docker",
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "50m",
    "max-file": "3"
  }
}
EOF
```

> **Note:** The `data-root` above assumes you have mounted a data disk at `/mnt/data`. If you are not using a data disk, remove the `data-root` line and Docker will use the default `/var/lib/docker`.

**Step 6 — Restart Docker to apply config:**
```bash
systemctl restart docker
docker info | grep "Docker Root Dir"
```

**Step 7 — Clone the repository:**
```bash
cd ~
git clone https://YOUR_GITHUB_TOKEN@github.com/YOUR_ORG/YOUR_REPO.git one_meristem_app
cd one_meristem_app
```

---

## GitHub Actions Runner Setup

The GitHub runner is a separate ECS instance that listens for GitHub Actions jobs and executes them.

**Step 1 — Install Docker on the runner server:**
```bash
curl -fsSL https://get.docker.com | sh
systemctl enable docker && systemctl start docker
```

**Step 2 — Install pack CLI on the runner:**
```bash
curl -sSL "https://github.com/buildpacks/pack/releases/download/v0.35.0/pack-v0.35.0-linux.tgz" \
  | tar -C /usr/local/bin/ --no-same-owner -xzv pack
```

**Step 3 — Install Java (both versions needed for CI):**
```bash
apt install -y openjdk-21-jdk
# Java 25 is installed automatically by the pipeline via actions/setup-java
```

**Step 4 — Install Maven:**
```bash
apt install -y maven
mvn -version
```

**Step 5 — Create a dedicated runner user:**
```bash
useradd -m -s /bin/bash githubrunner
usermod -aG docker githubrunner
```

**Step 6 — Download and configure the GitHub Actions runner:**

Go to your GitHub repository → Settings → Actions → Runners → New self-hosted runner → Linux → copy the commands shown and run them as the `githubrunner` user:

```bash
su - githubrunner
mkdir actions-runner && cd actions-runner
# Paste the download and configure commands from GitHub UI here
./config.sh --url https://github.com/YOUR_ORG/YOUR_REPO --token YOUR_RUNNER_TOKEN
```

**Step 7 — Install the runner as a system service:**
```bash
sudo ./svc.sh install
sudo ./svc.sh start
sudo ./svc.sh status
```

**Step 8 — Configure SSH access from runner to backend server:**

The CD pipeline SSHes from the runner into the backend server. Generate an SSH key on the runner and add the public key to the backend server:

```bash
# On the runner server
ssh-keygen -t ed25519 -C "github-runner" -f ~/.ssh/deploy_key -N ""
cat ~/.ssh/deploy_key.pub
```

Copy the public key output, then on the backend server:
```bash
# On the backend server
echo "PASTE_PUBLIC_KEY_HERE" >> ~/.ssh/authorized_keys
chmod 600 ~/.ssh/authorized_keys
```

Test the connection from the runner:
```bash
ssh -i ~/.ssh/deploy_key root@YOUR_BACKEND_SERVER_IP
```

---

## Security Group Configuration

Configure the following inbound rules on the **backend server's** Huawei Cloud security group:

| Port | Protocol | Source | Purpose |
|------|----------|--------|---------|
| 22 | TCP | Your IP only | SSH access |
| 22 | TCP | GitHub runner security group | SSH from CI/CD runner |
| 80 | TCP | 0.0.0.0/0 | HTTP (redirects to HTTPS) |
| 443 | TCP | 0.0.0.0/0 | HTTPS |
| 3000 | TCP | Your team IP only | Grafana monitoring UI |
| 8080 | TCP | 0.0.0.0/0 | HTTP unsecured access |
| 5432 | TCP | Backend server security group | PostgreSQL |
| 6379 | TCP | Backend server security group | Redis |
| 9092-9093 | TCP | Backend server security group | Kafka |
| 20000-20100 | TCP | Backend server security group | Microservice ports |

Configure the following inbound rules on the **GitHub runner server's** security group:

| Port | Protocol | Source | Purpose |
|------|----------|--------|---------|
| 22 | TCP | Your IP only | SSH access |

> **Security note:** Never open Redis, Postgres, Kafka or microservice ports to `0.0.0.0/0`. They should only be accessible within the same security group.

---

## Environment Variables

Create a `.env` file in the root of the cloned repository on the backend server:

```bash
vi ~/one_meristem_app/.env
```

Add the following variables — replace all placeholder values with your actual secrets:

```bash
# ----- GitHub Configuration -----
GITHUB_URL=https://github.com/YOUR_ORG/YOUR_REPO.git
GITHUB_USERNAME=YOUR_GITHUB_USERNAME
GITHUB_PASSWORD=YOUR_GITHUB_PERSONAL_ACCESS_TOKEN

# ----- Database Secrets -----
DB_USERNAME=admin
DB_PASSWORD=YOUR_STRONG_DB_PASSWORD

DB_NAME_USERS_SERVICE=users-service
DB_USERS_USERNAME=users-service
DB_USERS_PASSWORD=YOUR_STRONG_PASSWORD

DB_NAME_NOTIFICATION_SERVICE=notification-service
DB_NOTIFICATION_USERNAME=notification-service
DB_NOTIFICATION_PASSWORD=YOUR_STRONG_PASSWORD

DB_NAME_WALLET_SERVICE=wallet-service
DB_WALLET_USERNAME=wallet-service
DB_WALLET_PASSWORD=YOUR_STRONG_PASSWORD

DB_NAME_REPORT_SERVICE=report-service
DB_REPORT_USERNAME=report-service
DB_REPORT_PASSWORD=YOUR_STRONG_PASSWORD

DB_NAME_TRUSTEES_SERVICE=trustees-service
DB_TRUSTEES_USERNAME=trustees-service
DB_TRUSTEES_PASSWORD=YOUR_STRONG_PASSWORD

DB_NAME_WEALTH_SERVICE=wealth-service
DB_WEALTH_USERNAME=wealth-service
DB_WEALTH_PASSWORD=YOUR_STRONG_PASSWORD

# ----- Redis -----
REDIS_HOST=redis
REDIS_PASSWORD=YOUR_STRONG_REDIS_PASSWORD

# ----- Service URIs (internal Docker network) -----
CONFIG_SERVER_URI=http://config-server:8888
USER_SERVICE_URI=http://users-service:20020
NOTIFICATION_SERVICE_URI=http://notification-service:20030
WALLET_SERVICE_URI=http://wallet-service:20040
REPORT_SERVICE_URI=http://report-service:20050
TRUSTEES_SERVICE_URI=http://trustees-service:20060
WEALTH_SERVICE_URI=http://wealth-service:20070
KAFKA_HOST=kafka:9092

# ----- Encryption -----
ENCRYPTION_KEY=YOUR_ENCRYPTION_KEY
ENC_SALT=YOUR_ENC_SALT

# ----- Admin -----
ADMIN_USERNAME=admin
ADMIN_PASSWORD=YOUR_ADMIN_PASSWORD
H2_PASSWORD=YOUR_H2_PASSWORD

# ----- Monitoring -----
GRAFANA_PASSWORD=YOUR_STRONG_GRAFANA_PASSWORD

# ----- Additional app secrets -----
# Add remaining service-specific secrets here
# (SmileID, Dojah, Paystack, Pastel, Huawei OBS, Mailer, etc.)
```

### GitHub Actions Secrets

In your GitHub repository go to **Settings → Secrets and variables → Actions** and add:

| Secret Name | Description |
|-------------|-------------|
| `ECS_HOST` | Public IP of your backend server |
| `ECS_USER` | SSH user on backend server (e.g. `root`) |
| `ECS_SSH_KEY` | Private SSH key for runner → backend server connection |
| `GH_PAT` | GitHub Personal Access Token with repo read access |

---

## Data Disk Setup (Optional but Recommended)

> **This step is strongly recommended.** The system disk fills up quickly with Docker image layers, Kafka data and monitoring metrics. A dedicated data disk prevents the root partition from reaching 100%.

**Step 1 — Attach a data disk in Huawei Cloud console**, then on the server:

```bash
# Confirm the new disk is visible
lsblk -f
# You should see /dev/vdb with no filesystem
```

**Step 2 — Format and mount:**
```bash
mkfs.ext4 /dev/vdb
mkdir -p /mnt/data
mount /dev/vdb /mnt/data
```

**Step 3 — Make the mount permanent:**
```bash
# Get the UUID
blkid /dev/vdb

# Add to fstab — replace UUID_HERE with output from blkid
echo "UUID=UUID_HERE /mnt/data ext4 defaults,nofail 0 2" >> /etc/fstab
mount -a && echo "fstab OK"
```

**Step 4 — Configure Docker to use the data disk:**
```bash
cat > /etc/docker/daemon.json << 'EOF'
{
  "data-root": "/mnt/data/docker",
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "50m",
    "max-file": "3"
  }
}
EOF
systemctl restart docker
docker info | grep "Docker Root Dir"
# Should show: Docker Root Dir: /mnt/data/docker
```

---

## Server Directory Setup

Create the required directories on the backend server that are not in the repository:

```bash
cd ~/one_meristem_app

# NGINX directories
mkdir -p certs html acme

# NGINX vhost override (for WebSocket support)
mkdir -p vhost
touch vhost/YOUR_DOMAIN.com

# Application data persistence
mkdir -p data/postgres data/redis data/kafka data/kafka/secrets

# Copy monitoring configs from repo to active monitoring directory
cp -r monitoring-one-meristem/ monitoring
```

**Create monitoring persistence directories on the data disk:**
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

# Set permissions
chmod -R 777 /mnt/data/monitoring/loki
chmod -R 777 /mnt/data/monitoring/tempo
```

---

## PostgreSQL Initialisation

The `postgres/init-db.sh` script runs automatically the **first time** the PostgreSQL container starts on an empty data directory. It creates all databases and users for each microservice.

It will NOT run again on subsequent restarts — PostgreSQL skips initialisation if data already exists.

**To verify databases were created after first startup:**
```bash
# Wait 30 seconds after starting postgres
docker exec postgres psql -U admin -c "\l"
```

You should see databases for: `users-service`, `notification-service`, `wallet-service`, `report-service`, `trustees-service`, `wealth-service`.

**If you need to re-run the init script** (e.g. after a data wipe):
```bash
# Stop postgres, remove data, restart
docker stop postgres && docker rm postgres
rm -rf ~/one_meristem_app/data/postgres/*
sudo docker-compose -f docker-compose.yml up -d postgres
sleep 30
docker logs postgres --tail=20
```

---

## NGINX and SSL Configuration

The stack uses `nginxproxy/nginx-proxy` for automatic virtual host routing and `nginxproxy/acme-companion` for automatic SSL certificate provisioning via ZeroSSL.

**How it works:**

Each container that needs to be publicly accessible sets these environment variables:
```yaml
- VIRTUAL_HOST=your-domain.com
- VIRTUAL_PORT=20010
- LETSENCRYPT_HOST=your-domain.com
- LETSENCRYPT_EMAIL=your@email.com
```

nginx-proxy reads Docker labels and auto-generates NGINX config. acme-companion automatically requests and renews ZeroSSL certificates.

**WebSocket support** is enabled via `custom_headers.conf`:
```nginx
underscores_in_headers on;
proxy_pass_request_headers on;
proxy_read_timeout 3600s;
proxy_send_timeout 3600s;
```

And the vhost file at `vhost/YOUR_DOMAIN.com`:
```nginx
proxy_read_timeout 3600s;
proxy_send_timeout 3600s;
```

**Verify SSL is working:**
```bash
curl -v https://YOUR_DOMAIN.com/actuator/health 2>&1 | grep "SSL\|HTTP"
```

---

## CI Pipeline

The CI pipeline runs on **every Pull Request to `dev_env`** branch. It does not deploy anything — it only validates code quality.

**File:** `.github/workflows/ci.yaml`

### What it does:

**Job 1 — Detect Changed Services:**
- Compares files changed between commits
- Separates changed services into Java 21 and Java 25 groups
- Outputs which services need to be tested

**Job 2 — Test Java 21 Services** (runs in parallel with Job 3):
- Sets up JDK 21 (Temurin distribution)
- For each changed Java 21 service runs:
  - `mvn clean compile` — verifies code compiles
  - `mvn test-compile` — verifies tests compile (does not execute tests to avoid environment variable dependencies)
  - `mvn checkstyle:check` — runs static analysis if plugin is configured

**Job 3 — Test Java 25 Services** (runs in parallel with Job 2):
- Sets up JDK 25-ea (Early Access)
- Same steps as Job 2 but for Java 25 services

**Services by Java version:**

| Java 21 | Java 25 |
|---------|---------|
| config-server | wealth-service |
| cloud-gateway | wallet-service |
| users-service | notification-service |
| trustees-service | |
| report-service | |

**Pipeline passes when:** all changed services compile and test-compile successfully.

**Pipeline fails when:** any build or test-compile fails. Checkstyle failures are reported but do not fail the pipeline.

---

## CD Pipeline

The CD pipeline runs on every **push to `dev_env`** branch when files under `backend/**`, `docker-compose.yml` or `custom_headers.conf` change.

**File:** `.github/workflows/cd.yaml`

### What it does:

**Job 1 — Detect Changed Services:**
- Same detection logic as CI
- Also detects if `docker-compose.yml` itself changed

**Job 2 — Deploy:**

The runner SSHes into the backend server and runs these sections:

**Section 1 — Repository Sync:**
```bash
git fetch origin dev_env
git checkout dev_env
git reset --hard origin/dev_env
```

**Section 2 — Backup:**
```bash
docker-compose config > docker-compose.backup.yml
```

**Section 3 — Build Changed Services:**

Uses `pack` (Cloud Native Buildpacks) to build Docker images directly on the backend server. No registry is used — images are built and stored locally.

- Java 21 services use `paketobuildpacks/builder-jammy-base`
- Java 25 services use `paketobuildpacks/builder-noble-java-tiny`
- Build cache is stored at `/opt/buildpack-cache/SERVICE_NAME/` for fast rebuilds

**Section 4 — Deploy:**

Three scenarios:

| Scenario | Action |
|----------|--------|
| `docker-compose.yml` changed | Stop and recreate ALL services |
| Only service code changed | Stop and recreate ONLY the changed services |
| No relevant changes | Skip deployment |

**Section 5 — Health Check and Rollback:**

If deployment fails, the pipeline automatically rolls back using the backup compose file.

### Build Cache

Build cache persists between pipeline runs at `/opt/buildpack-cache/` on the backend server. This means subsequent builds of the same service are significantly faster — typically 2-3 minutes instead of 15-20 minutes.

> **Important:** The monitoring containers (`prometheus`, `grafana`, `loki`, `tempo`, `otel-collector`, `promtail`, `node-exporter`, `cadvisor`) are intentionally excluded from the deployment cycle. They are stateful infrastructure containers and should only be restarted manually when monitoring configuration changes.

---

## Microservices

All services communicate internally via Docker's `meristem-network` bridge network. The cloud-gateway is the only service exposed publicly via NGINX.

| Service | Port | Description |
|---------|------|-------------|
| `cloud-gateway` | 20010 | Spring Cloud Gateway — single entry point for all API requests from mobile and web clients |
| `config-server` | 20000 | Spring Cloud Config Server — serves centralised configuration to all services from a Git repository |
| `users-service` | 20020 | User management, authentication (OAuth2/JWT), KYC via SmileID, onboarding flows |
| `notification-service` | 20030 | Email, SMS and push notifications via Expo |
| `wallet-service` | 20040 | Wallet funding, balance management and payment processing via Paystack and Providus |
| `report-service` | 20050 | Investment reports, account statements and document generation |
| `trustees-service` | 20060 | Trustee and beneficiary management for investment accounts |
| `wealth-service` | 20070 | Investment portfolio management and wealth tracking |

### Supporting Infrastructure

| Service | Port | Description |
|---------|------|-------------|
| `postgres` | 5432 | PostgreSQL 15 — primary database, one schema per service |
| `redis` | 6379 | Redis 7 — caching and session management |
| `kafka` | 9092 | Apache Kafka — async event streaming between services |
| `consul` | 8500 | HashiCorp Consul — service discovery and health checking |

### Starting All Services

```bash
cd ~/one_meristem_app

# Start infrastructure first
sudo docker-compose -f docker-compose.yml up -d redis postgres kafka consul

# Wait for infrastructure to be healthy
sleep 60

# Start config server
sudo docker-compose -f docker-compose.yml up -d config-server

# Wait for config server to start
sleep 30

# Start all application services
sudo docker-compose -f docker-compose.yml up -d

# Verify all containers are running
docker ps
```

### Checking Service Health

```bash
# Check all containers
docker ps

# Check a specific service log
docker logs users-service --tail=50

# Check gateway health
curl https://YOUR_DOMAIN.com/actuator/health
```

---

## Monitoring Stack

The monitoring stack provides full observability across all containers and the host server.

### Components

| Component | Image | Purpose |
|-----------|-------|---------|
| Prometheus | `prom/prometheus:latest` | Metrics collection and 30-day storage |
| Grafana | `grafana/grafana:latest` | Visualisation dashboards |
| Loki | `grafana/loki:latest` | Log aggregation |
| Promtail | `grafana/promtail:2.6.1` | Docker log shipper → Loki |
| Tempo | `grafana/tempo:2.6.1` | Distributed tracing |
| OTel Collector | `otel/opentelemetry-collector-contrib:latest` | Telemetry routing |
| Node Exporter | `prom/node-exporter:latest` | Host CPU, RAM, disk metrics |
| cAdvisor | `gcr.io/cadvisor/cadvisor:latest` | Per-container metrics |

### Starting the Monitoring Stack

```bash
cd ~/one_meristem_app

sudo docker-compose -f docker-compose.yml up -d \
  node-exporter cadvisor loki tempo otel-collector prometheus grafana promtail
```

### Verify Monitoring is Working

```bash
# Check all monitoring containers are running
docker ps | grep -E "prometheus|grafana|loki|tempo|otel|promtail|node-exporter|cadvisor"

# Verify Loki is receiving logs from all containers
docker exec prometheus wget -qO- "http://loki:3100/loki/api/v1/label/container_name/values" 2>&1

# Check Prometheus targets
docker exec prometheus wget -qO- http://localhost:9090/api/v1/targets | \
  python3 -m json.tool | grep -E '"health"|"job"'
```

### Enabling Spring Boot Metrics

For Prometheus to scrape metrics from Spring Boot services, the backend config must have the prometheus endpoint enabled. In `backend/meriapp-config/application-dev.yaml`:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: 'health,prometheus'
  endpoint:
    health:
      show-details: always
      access: read_only
    prometheus:
      access: read_only    # ← must be read_only, NOT none
```

### Monitoring Config Files

All monitoring configuration is version-controlled in `monitoring-one-meristem/`. After cloning, copy to the active monitoring directory:

```bash
cp -r monitoring-one-meristem/ ~/one_meristem_app/monitoring
```

The Prometheus scrape config uses the correct actuator paths per service context:

| Service | Metrics Path |
|---------|-------------|
| cloud-gateway | `/actuator/prometheus` |
| users-service | `/api/users/actuator/prometheus` |
| notification-service | `/api/notification/actuator/prometheus` |
| wallet-service | `/api/wallets/actuator/prometheus` |
| report-service | `/api/reports/actuator/prometheus` |
| trustees-service | `/api/trustees/actuator/prometheus` |
| wealth-service | `/api/wealths/actuator/prometheus` |
| config-server | `/actuator/prometheus` |

---

## Grafana Access and User Management

### Accessing Grafana

Grafana is accessible on port 3000:

```
http://YOUR_SERVER_IP:3000
```

> **Note:** Port 3000 must be open in your Huawei Cloud security group. Restrict the source to your team's IP addresses for security.

Default admin credentials:
```
Username: admin
Password: value of GRAFANA_PASSWORD in your .env file
```

### Adding Datasources

Datasources are auto-provisioned from `monitoring/grafana/provisioning/datasources/datasources.yml`. If they don't appear automatically, add them manually:

1. Go to **Connections → Data sources → Add new data source**

| Datasource | Type | URL |
|------------|------|-----|
| Prometheus | Prometheus | `http://prometheus:9090` |
| Loki | Loki | `http://loki:3100` |
| Tempo | Tempo | `http://tempo:3200` |

Click **Save & test** on each — all should show green.

### Importing Dashboards

Go to **Dashboards → New → Import** and import by ID:

| Dashboard | ID | Datasource | Shows |
|-----------|-----|------------|-------|
| Server Health (Node Exporter Full) | `1860` | Prometheus | Host CPU, RAM, disk, network |
| Container Health | `15798` | Prometheus | Per-container CPU and memory |

### Searching Logs (Loki Explore)

1. Click the **compass icon (Explore)** in the left sidebar
2. Select **Loki** from the datasource dropdown
3. Use label filters to search:

```logql
# All logs from a specific service
{container_name="users-service"}

# Filter by log level
{container_name="users-service"} |= "ERROR"
{container_name="cloud-gateway"} |= "403"

# Search across multiple services
{container_name=~"users-service|cloud-gateway|wallet-service"}
```

**Available container names:**
`users-service`, `cloud-gateway`, `notification-service`, `wallet-service`, `report-service`, `trustees-service`, `wealth-service`, `config-server`, `postgres`, `redis`, `kafka`, `consul`, `nginx`, `nginx-acme`

### Creating Dev Team Users

1. Go to **☰ → Administration → Users and access → Users → New user**
2. Fill in name, email, username and password
3. Set role to **Editor** — can view dashboards and use Explore but cannot change server settings

Share with your dev team:
```
URL: http://YOUR_SERVER_IP:3000
Username: their username
Password: password you set
```

---

## Troubleshooting

### Services not starting after deployment

```bash
# Check which containers are running
docker ps -a

# Check logs of a failed service
docker logs SERVICE_NAME --tail=50

# Restart a specific service
sudo docker-compose -f docker-compose.yml restart SERVICE_NAME
```

### Config server not connecting to GitHub

```bash
docker logs config-server --tail=30
# Check GITHUB_URL, GITHUB_USERNAME, GITHUB_PASSWORD in .env
```

### SSL certificate not issued

```bash
docker logs nginx-acme --tail=30
# Ensure port 80 is open and your domain DNS is pointing to the server
```

### WebSocket connections failing

Check the vhost file exists and contains timeout settings:
```bash
cat ~/one_meristem_app/vhost/YOUR_DOMAIN.com
# Should contain: proxy_read_timeout 3600s; proxy_send_timeout 3600s;
```

Check the custom headers config:
```bash
docker exec nginx nginx -t
docker exec nginx nginx -s reload
```

### Loki permission denied on startup

```bash
chmod -R 777 /mnt/data/monitoring/loki
sudo docker-compose -f docker-compose.yml restart loki
```

### Tempo fails to start

Always use the pinned version `grafana/tempo:2.6.1`. Do not use `latest` — Tempo v2.7+ changed its default ingestion to Kafka which breaks the single-node setup.

### PostgreSQL init script not running

The init script only runs on a completely empty data directory. If postgres data already exists, the script is skipped. This is correct behaviour — your existing databases are intact.

### Build cache not found on runner

The build cache lives at `/opt/buildpack-cache/` on the **backend server**, not the runner. The runner SSHes into the backend server to run builds. Verify the cache exists:

```bash
# On the backend server
ls -la /opt/buildpack-cache/
du -sh /opt/buildpack-cache/*/
```

### Checking resource usage across all containers

```bash
docker stats --no-stream --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.MemPerc}}"
```

---

## Quick Reference

### Useful Commands

```bash
# Start everything
cd ~/one_meristem_app && sudo docker-compose -f docker-compose.yml up -d

# Stop everything
sudo docker-compose -f docker-compose.yml down

# Restart a single service
sudo docker-compose -f docker-compose.yml restart users-service

# View logs
docker logs SERVICE_NAME -f --tail=100

# Check disk usage
df -h && docker system df

# Check all container resource usage
docker stats --no-stream

# Reload NGINX without downtime
docker exec nginx nginx -s reload

# Test NGINX config
docker exec nginx nginx -t
```

### Pipeline Triggers

| Action | Pipeline |
|--------|---------|
| Open a PR to `dev_env` | CI runs — compile + static analysis |
| Push to `dev_env` (backend code) | CD runs — build changed services + deploy |
| Push to `dev_env` (docker-compose.yml) | CD runs — restart ALL services |
| Push to `dev_env` (monitoring configs only) | No pipeline — restart monitoring manually |

### Port Reference

| Port | Service | Access |
|------|---------|--------|
| 80 | NGINX HTTP | Public |
| 443 | NGINX HTTPS | Public |
| 3000 | Grafana | Team only |
| 8500 | Consul UI | Internal |
| 20010 | cloud-gateway | Internal (via NGINX) |
| 20020 | users-service | Internal |
| 20030 | notification-service | Internal |
| 20040 | wallet-service | Internal |
| 20050 | report-service | Internal |
| 20060 | trustees-service | Internal |
| 20070 | wealth-service | Internal |
