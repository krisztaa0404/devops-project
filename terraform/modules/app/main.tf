terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0.0"
    }
  }
}

resource "docker_image" "app" {
    name = var.image_name
}

resource "docker_container" "app" {
    name  = "springboot-app"
    image = docker_image.app.name

    ports {
        internal = 8080
        external = var.external_port
    }

    networks_advanced {
        name = var.network_name
    }

    log_driver = "syslog"
    log_opts = {
        "syslog-address" = "udp://127.0.0.1:514"
        "tag" = "springboot-app"
        "syslog-facility" = "local0"
    }

}