terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0.0"
    }
  }
}

resource "docker_image" "elasticsearch" {
  name = "docker.elastic.co/elasticsearch/elasticsearch:8.15.0"
}

resource "docker_volume" "elasticsearch_data" {
  name = "elasticsearch_data"
}

resource "docker_container" "elasticsearch" {
  name  = "elasticsearch"
  image = docker_image.elasticsearch.name

  ports {
    internal = 9200
    external = var.external_port
  }

  env = [
    "discovery.type=single-node",
    "ES_JAVA_OPTS=-Xms512m -Xmx512m",
    "xpack.security.enabled=false",
    "xpack.security.enrollment.enabled=false"
  ]

  volumes {
    volume_name    = docker_volume.elasticsearch_data.name
    container_path = "/usr/share/elasticsearch/data"
  }

  networks_advanced {
    name = var.network_name
  }
}