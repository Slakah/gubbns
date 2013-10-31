package models

import org.joda.time.DateTime
import com.fasterxml.jackson.annotation.{JsonProperty, JsonCreator}

@JsonCreator
case class Post(
               @JsonProperty("title") title: String,
               @JsonProperty("content") body: String,
               @JsonProperty("published") published: DateTime,
               @JsonProperty("author") author: String)