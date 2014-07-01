package services

import play.twirl.api.Html
import org.pegdown.PegDownProcessor
import scala.concurrent._
import play.api.libs.concurrent.Execution.Implicits._

trait MarkdownService {
  def apply(input: CharSequence): Future[Html]
}

trait MarkdownServiceComponent {
  def markdown: MarkdownService
}

trait PegdownServiceComponent extends MarkdownServiceComponent {
  override implicit val markdown: MarkdownService = PegdownService

  object PegdownService extends MarkdownService {
    override def apply(input: CharSequence): Future[Html] = Future {
      Html(new PegDownProcessor().markdownToHtml(input.toString))
    }
  }
}