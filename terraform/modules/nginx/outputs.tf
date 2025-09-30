output "nginx_landing_page" {
  description = "URL to access the main landing page"
  value       = "http://devops-project.test"
}

output "grafana_url" {
  description = "URL to access Grafana via subdomain"
  value       = "http://grafana.devops-project.test"
}

output "prometheus_url" {
  description = "URL to access Prometheus via subdomain"
  value       = "http://prometheus.devops-project.test"
}

output "kibana_url" {
  description = "URL to access Kibana via subdomain"
  value       = "http://kibana.devops-project.test"
}