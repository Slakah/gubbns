package models

import org.joda.time.DateTime
import com.fasterxml.jackson.annotation.{JsonIdentityInfo, JsonProperty}

case class Post(
  @JsonProperty("_id") id: String,
  @JsonProperty("_rev") rev: String,
  @JsonProperty("title") title: String,
  @JsonProperty("content") content: String,
  @JsonProperty("published") published: DateTime,
  @JsonProperty("author") author: String,
  @JsonProperty("typeId") typeId: String)
