variable "image_name" {
  type        = string
  description = "Docker image name for Spring Boot app"
}

variable "external_port" {
  type        = number
  default     = 8080
  description = "Port number for the application"
}

variable "network_name" {
  type        = string
  description = "Docker network name"
}