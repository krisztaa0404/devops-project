variable "external_port" {
  description = "Host port for Nginx"
  type        = number
  default     = 80
}

variable "network_name" {
  description = "Docker network name"
  type        = string
}