#!/bin/bash

# Terraform Infrastructure Setup Script
# This script initializes and applies the Terraform configuration using Docker

echo "🚀 Starting Terraform infrastructure setup..."

# Define terraform as a function instead of alias (works in WSL + Git Bash)
terraform() {
  docker run -it --rm \
    -v "$PWD":/workspace \
    -v /var/run/docker.sock:/var/run/docker.sock \
    -w /workspace \
    hashicorp/terraform:light "$@"
}

# Initialize Terraform
echo "🔧 Initializing Terraform..."
terraform init -input=false

# Validate configuration
echo "✅ Validating Terraform configuration..."
terraform validate

# Plan the deployment
echo "📋 Planning Terraform deployment..."
terraform plan -var-file=secrets.tfvars -input=false

# Apply the configuration
echo "🏗️  Applying Terraform configuration..."
terraform apply -var-file=secrets.tfvars -auto-approve -input=false

echo ""
echo "✅ Infrastructure deployment completed!"
echo ""
echo "📊 Access your services:"
echo "  🌐 Spring Boot App: http://localhost:8080"
echo "  📈 Prometheus: http://localhost:9090 or http://prometheus.devops-project.test"
echo "  📊 Grafana: http://localhost:3001 or http://grafana.devops-project.test"
echo "  🔍 Elasticsearch: http://localhost:9200"
echo "  📈 Elasticsearch Exporter: http://localhost:9114/metrics"
echo "  🔍 Kibana: http://localhost:5601 or http://kibana.devops-project.test"
echo "  🏠 Landing page: http://devops-project.test"
echo ""
echo "🔐 Grafana login: admin / [your_grafana_admin_password]"
echo ""
echo "To destroy the infrastructure, run:"
echo "  terraform destroy -var-file=secrets.tfvars -auto-approve"