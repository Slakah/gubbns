package models

import org.joda.time.DateTime
import com.fasterxml.jackson.annotation.JsonProperty

case class Post(
  @JsonProperty("_id") _id: String,
  @JsonProperty("_rev") _rev: String,
  @JsonProperty("type") typeId: String,
  @JsonProperty("title") title: String,
  @JsonProperty("content") content: String,
  @JsonProperty("published") published: DateTime,
  @JsonProperty("author") author: String
)