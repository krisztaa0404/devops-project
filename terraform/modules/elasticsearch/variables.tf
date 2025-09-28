variable "external_port" {
  type    = number
  default = 9200
}

variable "network_name" {
  type        = string
  description = "Docker network name"
}