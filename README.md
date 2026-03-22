# one_meristem_app

# Staging Environment Deployment Guide

## Overview

The **Staging environment** is used to validate application changes in a production-like setup before release. This is actually for the Mobile Backend Deployment  
Deployments follow a **GitOps-based approach** using **Argo CD**, with a clear separation between **Continuous Integration (CI)** and **Continuous Deployment (CD)** workflows.

- **CI** is responsible for building, testing, scanning, and publishing container images
- **CD** updates Helm and Argo CD manifests
- **Argo CD** acts as the deployment engine and consumes Git as the single source of truth

---

## Technology Stack

- **CI/CD Platform:** GitHub Actions  
- **CD Strategy:** GitOps (Argo CD)  
- **Container Registry:** Huawei Software Repository for Container (SWR)  
- **Container Orchestration:** Kubernetes (Huawei CCE)  
- **Packaging:** Helm  
- **Application Architecture:** Microservice-based backend services

---

# Continuous Integration (CI)

### Workflow

```text
.github/workflows/Stag_CI_PR.yml
```
---

## Trigger

Pull requests targeting the staging branch

## CI Responsibilities

1. Change Detection
Identifies which microservices were modified in the PR
Ensures only affected services are processed

2. Code Testing & Quality Gates
For each changed microservice:
Unit tests
Static code analysis
SpotBugs checks
Additional configured validations
Test and analysis reports are uploaded as workflow artifacts

3. Build Stage
Builds Docker images using standardized build scripts
Produces versioned container images per microservice

4. Image Security Scanning
Scans built images for vulnerabilities
Prevents unsafe images from proceeding further

5. Image Tagging & Push
Tags validated images
Pushes images to Huawei SWR
Images become available for deployment via Argo CD

---


# Continuous Deployment (CD)
## Workflow

```text
.github/workflows/STAG_CD_Push.yml
```
---
Trigger
Pushes to the stag_env branch

CD Responsibilities
This workflow automates the continuous deployment process for the One-Meristem Mobile-backend in the staging environment.

It performs the following actions:

1. Change Detection

Identifies which microservices have updated container images

2. Helm & Argo CD Values Substitution

Substitutes environment-specific variables
Updates Helm values (e.g., image tags, service configuration)
Applies changes only to affected microservices

3. Source-of-Truth Update

Commits updated Helm and Argo CD values back to the repository
The Git repository remains the single source of truth
Deployment with Argo CD
Argo CD continuously monitors the Git repository
Updated Helm values trigger reconciliation
Argo CD deploys changes declaratively to the staging cluster

Auto-sync is enabled for just the stagging environment but willl be disabled on production environment to maintain production-grade control and observability

End-to-End Flow Summary
Pull Request → CI (Build, Test, Scan, Push Image)
             → CD (Update Helm & Argo Values)
             → Git Repository (Source of Truth)
             → Argo CD Sync
             → Staging Kubernetes Cluster

Key Benefits

Clear separation of CI and CD responsibilities

GitOps-driven, declarative deployments

Efficient microservice-level change handling

Strong auditability and rollback capability

Consistent staging environment aligned with production practices
