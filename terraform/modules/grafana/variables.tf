variable "external_port" {
  type    = number
  default = 3000
}

variable "network_name" {
  type        = string
  description = "Docker network name"
}

variable "grafana_admin_password" {
  description = "Grafana admin password"
  type        = string
}