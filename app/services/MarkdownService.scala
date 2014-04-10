package services

import play.api.templates.Html
import org.pegdown.PegDownProcessor
import scala.concurrent._
import play.api.libs.concurrent.Execution.Implicits._

trait MarkdownServiceComponent {
  this: MarkdownServiceComponent =>

  implicit val markdown: MarkdownService

  class MarkdownServiceImpl extends MarkdownService {
    def apply(input: CharSequence): Future[Html] = markdown.apply(input)
  }


}

trait MarkdownService {
  def apply(input: CharSequence): Future[Html]
}

trait PegdownServiceComponent extends MarkdownServiceComponent {
  val markdown: MarkdownService = PegdownService

  object PegdownService extends MarkdownService {
    override def apply(input: CharSequence): Future[Html] = future {
      Html(new PegDownProcessor().markdownToHtml(input.toString))
    }
  }
}