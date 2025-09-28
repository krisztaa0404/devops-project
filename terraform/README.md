Add# Infrastructure Services

This Terraform configuration deploys a complete logging and monitoring stack using Docker containers.

## Services

### **Elasticsearch**
- **Port**: 9200
- **Purpose**: Search and analytics engine for log storage

### **Kibana**
- **Port**: 5601
- **Purpose**: Web UI for visualizing logs and data

### **Filebeat**
- **Purpose**: Collects Spring Boot application logs and ships them to Elasticsearch
- **Config**: Monitors container logs with JSON parsing for structured logging

### **Prometheus**
- **Port**: 9090
- **Purpose**: Metrics collection and monitoring

### **Grafana**
- **Port**: 3001
- **Purpose**: Metrics visualization and dashboards

### **App Container**
- **Purpose**: Sample application for generating logs and metrics
- **Integration**: Logs shipped via Filebeat, metrics scraped by Prometheus

## Usage

```bash
# Quick start
./run.sh

# Manual deployment
terraform init
terraform plan
terraform apply
```

Access services at:
- Kibana: http://localhost:5601
- Grafana: http://localhost:3001
- Prometheus: http://localhost:9090

## Troubleshooting

### Docker Desktop with containerd

If using Docker Desktop with containerd enabled, you may encounter build issues with the Terraform Docker provider.

**Issue**: https://github.com/kreuzwerker/terraform-provider-docker/issues/534

**Solution**: Change Docker Desktop settings:
1. Open Docker Desktop Settings
2. Go to Settings → General
3. Disable "Use containerd for pulling and storing images"
4. Restart Docker Desktop

This switches back to the traditional Docker image storage which is compatible with the Terraform provider.

## Tested Environment

This configuration has been tested on:

**System**: macOS 15.6.1 (Apple Silicon - arm64)
**Docker**: Version 27.4.0
**Terraform**: Version 1.8.0
**Docker Desktop**: With containerd disabled (traditional image storage)