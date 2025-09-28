terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0.0"
    }
  }
}

resource "docker_image" "filebeat" {
  name = "custom-filebeat:latest"
  build {
    context = "."
    dockerfile = "Dockerfile_filebeat"
    no_cache = true
  }
}

resource "docker_container" "filebeat" {
  name  = "filebeat"
  image = docker_image.filebeat.image_id

  volumes {
    host_path      = "/var/run/docker.sock"
    container_path = "/var/run/docker.sock"
    read_only      = true
  }

  volumes {
    host_path      = "/var/lib/docker/containers"
    container_path = "/var/lib/docker/containers"
    read_only      = true
  }

  networks_advanced {
    name = var.network_name
  }

  restart = "unless-stopped"
}