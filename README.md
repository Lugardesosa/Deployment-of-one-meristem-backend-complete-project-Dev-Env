# Production Environment Deployment Guide

## Overview

The **Production environment** hosts the live One-Meristem Mobile Backend platform and is designed for **stability, security, and controlled change management**.  
Deployments follow a **strict GitOps model** using **Argo CD**, with additional safeguards compared to staging.

- **CI** ensures only tested, scanned, and approved images are eligible for production
- **CD** updates production-specific Helm and Argo CD manifests
- **Argo CD** deploys changes declaratively from Git, which remains the single source of truth

---

## Technology Stack

- **CI/CD Platform:** GitHub Actions  
- **CD Strategy:** GitOps (Argo CD)  
- **Container Registry:** Huawei Software Repository for Container (SWR)  
- **Container Orchestration:** Kubernetes (Huawei CCE)  
- **Packaging:** Helm  
- **Application Architecture:** Microservice-based backend services

---

## Continuous Integration (CI)

### Workflow

```text
.github/workflows/Prod_CI_PR.yml
```
---

## Trigger

Pull requests targeting the production branch

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
Pushes to the prod_env branch

CD Responsibilities
This workflow automates controlled deployment to production while enforcing governance and safety.

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

Auto-sync is disabled on production environment to maintain production-grade control and observability, Deployments are manual-sync only

End-to-End Flow Summary
Staging-Validated Image
   → Production CI Validation
   → CD (Update Helm & Argo CD Values)
   → Git Repository (Source of Truth)
   → Manual Argo CD Sync
   → Production Kubernetes Cluster

Production Safeguards

Manual approvals for deployments

Auto-sync disabled

Immutable container images

Full audit trail via Git history

Controlled rollback via Git revert


Key Benefits

Strong governance and compliance alignment

Predictable and controlled releases

Clear separation of duties

GitOps-driven rollback and traceability

Production stability without sacrificing delivery speed
