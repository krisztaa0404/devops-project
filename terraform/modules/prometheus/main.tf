terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0.0"
    }
  }
}

resource "docker_image" "prometheus" {
  name = "custom-prometheus:latest"
  build {
    context = "."
    dockerfile = "Dockerfile_prometheus"
    no_cache = true
  }
}

resource "docker_volume" "prometheus_data" {
  name = "prometheus_data"
}

resource "docker_container" "prometheus" {
  name  = "prometheus"
  image = docker_image.prometheus.name

  ports {
    internal = 9090
    external = var.external_port
  }

  volumes {
    volume_name    = docker_volume.prometheus_data.name
    container_path = "/prometheus"
  }

  networks_advanced {
    name = var.network_name
  }
}