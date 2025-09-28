terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0.0"
    }
  }
}

resource "docker_image" "elasticsearch_exporter" {
  name = "quay.io/prometheuscommunity/elasticsearch-exporter:latest"
}

resource "docker_container" "elasticsearch_exporter" {
  name  = "elasticsearch_exporter"
  image = docker_image.elasticsearch_exporter.image_id

  ports {
    internal = 9114
    external = var.external_port
  }

  command = [
    "--es.uri=${var.elasticsearch_url}",
    "--es.all",
    "--es.indices",
    "--es.shards",
    "--es.timeout=30s",
  ]

  networks_advanced {
    name = var.network_name
  }

  restart = "unless-stopped"
}