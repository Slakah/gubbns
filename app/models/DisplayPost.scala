package models

import org.joda.time.DateTime
import play.twirl.api.Html
import util.MarkdownProcessor

case class DisplayPost(title: String,
                        markdownContent: Html,
                        published: DateTime,
                        author: String)

object DisplayPost {
  def apply(post: Post)(implicit markdown: MarkdownProcessor): DisplayPost = {
     DisplayPost(post.title, markdown(post.content), post.published, post.author)
  }
}