package models

import org.joda.time.DateTime

class BlogPost(val title: String, val content: String, val published: DateTime, val author: String)

object BlogPostFixture extends BlogPost("title","content", DateTime.now, "James") {
}
