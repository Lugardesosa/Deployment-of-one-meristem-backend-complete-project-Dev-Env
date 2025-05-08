#!/bin/bash
# refresh-certificate.sh

# Download latest certificate
az keyvault secret show --vault-name "one-meristem-vault" --name "one-meristem" --query "value" -o tsv > /etc/ssl/certs/certificate.pem

# Restart your container to pick up the new certificate
#Restart with Clean Build 
sudo docker-compose down
sudo docker volume prune -f
sudo docker-compose up 
