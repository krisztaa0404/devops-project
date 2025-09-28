variable "external_port" {
  description = "External port for Kibana web interface"
  type        = number
  default     = 5601
}

variable "network_name" {
  description = "Docker network name for Kibana container"
  type        = string
}

variable "elasticsearch_url" {
  description = "Elasticsearch URL for Kibana to connect to"
  type        = string
  default     = "http://elasticsearch:9200"
}