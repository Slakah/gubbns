package models

import org.joda.time.DateTime
import play.twirl.api.Html
import services.MarkdownService

case class DisplayPost(title: String,
                        markdownContent: Html,
                        published: DateTime,
                        author: String)

object DisplayPost {
  def apply(post: Post)(implicit markdown: MarkdownService): DisplayPost = {
     DisplayPost(post.title, markdown(post.content), post.published, post.author)
  }
}