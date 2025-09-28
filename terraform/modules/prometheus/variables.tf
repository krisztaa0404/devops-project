variable "external_port" {
  type    = number
  default = 9090
}

variable "network_name" {
  type        = string
  description = "Docker network name"
}