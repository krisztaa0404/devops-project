variable "network_name" {
  type        = string
  description = "Docker network name"
}

variable "elasticsearch_host" {
  type        = string
  description = "Elasticsearch host URL"
  default     = "elasticsearch:9200"
}