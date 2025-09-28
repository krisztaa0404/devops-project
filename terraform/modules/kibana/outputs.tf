output "kibana_url" {
  description = "URL to access Kibana web interface"
  value       = "http://localhost:${var.external_port}"
}

output "container_name" {
  description = "Name of the Kibana Docker container"
  value       = docker_container.kibana.name
}

output "container_id" {
  description = "ID of the Kibana Docker container"
  value       = docker_container.kibana.id
}