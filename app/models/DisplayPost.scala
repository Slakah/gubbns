package models

import org.joda.time.DateTime
import play.api.templates.Html
import services.MarkdownService
import scala.concurrent._
import play.api.libs.concurrent.Execution.Implicits._

case class DisplayPost(title: String,
                        markdownContent: Html,
                        published: DateTime,
                        author: String)

object DisplayPost {

  def apply(post: Post)(implicit markdown: MarkdownService): Future[DisplayPost] = {
    markdown(post.content).map({
      new DisplayPost(post.title, _, post.published, post.author)
    })
  }
}