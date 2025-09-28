output "elasticsearch_url" {
  value = "http://elasticsearch:9200"
}

output "container_name" {
  value = docker_container.elasticsearch.name
}
