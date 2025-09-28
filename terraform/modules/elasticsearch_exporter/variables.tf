variable "external_port" {
  type        = number
  default     = 9114
  description = "External port for Elasticsearch Exporter"
}

variable "network_name" {
  type        = string
  description = "Docker network name"
}

variable "elasticsearch_url" {
  type        = string
  description = "Elasticsearch URL"
  default     = "http://elasticsearch:9200"
}