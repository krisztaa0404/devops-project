
resource "docker_network" "app_network" {
  name = "devops-network"
}

module "app" {
  source        = "./modules/app"
  image_name    = "ghcr.io/${var.github_username}/${var.github_project_name}/spring-boot-app:latest"
  external_port = var.app_port
  network_name  = docker_network.app_network.name
}

module "prometheus" {
  source        = "./modules/prometheus"
  external_port = var.prometheus_port
  network_name  = docker_network.app_network.name

  depends_on = [module.app, module.elasticsearch_exporter]
}

module "grafana" {
  source                 = "./modules/grafana"
  external_port          = var.grafana_port
  network_name           = docker_network.app_network.name
  grafana_admin_password = var.grafana_admin_password

  depends_on = [module.prometheus, module.elasticsearch]
}

module "filebeat" {
  source           = "./modules/filebeat"
  network_name     = docker_network.app_network.name
  elasticsearch_host = "elasticsearch:9200"

  depends_on = [module.elasticsearch]
}

module "elasticsearch" {
  source        = "./modules/elasticsearch"
  external_port = var.elasticsearch_port
  network_name  = docker_network.app_network.name
}

module "elasticsearch_exporter" {
  source           = "./modules/elasticsearch_exporter"
  external_port    = var.elasticsearch_exporter_port
  network_name     = docker_network.app_network.name
  elasticsearch_url = "http://elasticsearch:9200"

  depends_on = [module.elasticsearch]
}

module "kibana" {
  source        = "./modules/kibana"
  external_port = var.kibana_port
  network_name  = docker_network.app_network.name
  elasticsearch_url = "http://elasticsearch:9200"

  depends_on = [module.elasticsearch]
}

