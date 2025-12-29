# Development Environment Deployment Guide

## Overview

The **Development (Dev) environment** is used for active development and early validation of changes for the **One-Meristem Mobile-backend**.  
Unlike staging and production, the dev environment does **not** use Kubernetes or Argo CD.  
Instead, it relies on **Docker Compose** deployed to a **Huawei ECS virtual machine**, enabling faster iteration and simpler debugging.

- **CI** validates code quality and correctness
- **CD** deploys services directly to an ECS server using Docker Compose
- Designed for speed, flexibility, and developer feedback

---

## Technology Stack

- **CI/CD Platform:** GitHub Actions  
- **Deployment Target:** Huawei ECS (Virtual Machine)  
- **Container Runtime:** Docker  
- **Service Orchestration:** Docker Compose  
- **Application Architecture:** Microservice-based mobile backend services

---

# Continuous Integration (CI)

### Workflow

```text
.github/workflows/ci_test_checks.yml
```

---

Trigger

Pull requests or pushes to the development branch

CI Responsibilities
1. Change Detection

Detects which microservices were modified

Limits execution to only affected services

2. Code Testing & Validation

For each changed microservice:

Unit tests

Static code analysis

Framework-specific validations

Test results and logs are published as workflow artifacts for visibility.


# Continuous Deployment (CD)

### Workflow

```text
.github/workflows/deploy_to_Huawei_cloud.yml
```
---

Trigger
Pushes to the development branch

### CD Responsibilities

This workflow automates deployment of the One-Meristem Mobile-backend to the development environment hosted on Huawei ECS.

1. Change Detection
Identifies which microservices were modified
Determines which services require redeployment

2. Build & Deployment on ECS
Connects securely to the Huawei ECS server
Builds Docker images directly on the server
Updates and deploys services using docker-compose.yml
Restarts only the affected microservices

Deployment Flow Summary
Code Change
   → CI (Detect Changes, Run Tests)
   → CD (Detect Changes, Build Images on ECS)
   → Docker Compose Deployment
   → Development Environment (Huawei ECS)

Key Characteristics of the Dev Environment

Fast feedback loop for developers
Simplified infrastructure
Easy access to logs and running containers
Suitable for debugging and feature development
