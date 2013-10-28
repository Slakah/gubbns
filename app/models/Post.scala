package models

import org.joda.time.DateTime
import org.codehaus.jackson.annotate.JsonProperty

case class Post(
               @JsonProperty("title") title: String,
               @JsonProperty("content") body: String,
               @JsonProperty("published") published: DateTime,
               @JsonProperty("author") author: String)