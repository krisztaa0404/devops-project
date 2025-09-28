terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0.0"
    }
  }
}

resource "docker_image" "kibana" {
  name = "custom-kibana:latest"
  build {
    context = "."
    dockerfile = "Dockerfile_kibana"
    no_cache = true
  }
}

resource "docker_volume" "kibana_data" {
  name = "kibana_data"
}

resource "docker_container" "kibana" {
  name  = "kibana"
  image = docker_image.kibana.image_id

  ports {
    internal = 5601
    external = var.external_port
  }

  env = [
    "ELASTICSEARCH_HOSTS=${var.elasticsearch_url}",
    "ELASTICSEARCH_USERNAME=kibana_system",
    "ELASTICSEARCH_PASSWORD=",
    "XPACK_SECURITY_ENABLED=false",
    "XPACK_ENCRYPTEDSAVEDOBJECTS_ENCRYPTIONKEY=a7a6311933d3503b89bc2dbc36572c33a6c10925682e591bffcab6911c06786d"
  ]

  volumes {
    volume_name    = docker_volume.kibana_data.name
    container_path = "/usr/share/kibana/data"
  }

  networks_advanced {
    name = var.network_name
  }

  restart = "unless-stopped"
}