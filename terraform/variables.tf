variable "github_username" {
  description = "GitHub username"
  type = string
  default = "krisztaa0404"
}

variable "github_project_name" {
  description = "GitHub project name"
  type = string
  default = "devops-project"
}

variable "app_port" {
  description = "Host port for Spring Boot app"
  type        = number
  default     = 8080
}

variable "prometheus_port" {
  description = "Host port for Prometheus"
  type        = number
  default     = 9090
}

variable "elasticsearch_port" {
  description = "Host port for Elasticsearch"
  type        = number
  default     = 9200
}

variable "grafana_port" {
  description = "Host port for Grafana"
  type        = number
  default     = 3001
}

variable "grafana_admin_password" {
  description = "Grafana admin password"
  type        = string
  sensitive   = true
}

variable "elasticsearch_exporter_port" {
  description = "Host port for Elasticsearch Exporter"
  type        = number
  default     = 9114
}

variable "kibana_port" {
  description = "Host port for Kibana"
  type        = number
  default     = 5601
}

variable "nginx_port" {
  description = "Host port for Nginx"
  type        = number
  default     = 80
}
