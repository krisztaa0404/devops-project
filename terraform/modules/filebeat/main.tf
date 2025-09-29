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

  ports {
    internal = 514
    external = 514
    protocol = "udp"
  }

  volumes {
    host_path      = "/var/run/docker.sock"
    container_path = "/var/run/docker.sock"
    read_only      = true
  }

  networks_advanced {
    name = var.network_name
  }

  restart = "unless-stopped"
}