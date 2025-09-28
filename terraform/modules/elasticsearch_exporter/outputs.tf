output "exporter_url" {
  value = "http://localhost:${var.external_port}/metrics"
}

output "container_name" {
  value = docker_container.elasticsearch_exporter.name
}

output "internal_url" {
  value = "http://elasticsearch_exporter:9114/metrics"
}