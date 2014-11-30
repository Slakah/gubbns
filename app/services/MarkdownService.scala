package services

import play.twirl.api.Html
import org.pegdown.PegDownProcessor

trait MarkdownService {
  def apply(input: CharSequence): Html
}

trait MarkdownServiceComponent {
  def markdown: MarkdownService
}

trait PegdownServiceComponent extends MarkdownServiceComponent {
  override implicit val markdown: MarkdownService = PegdownService

  object PegdownService extends MarkdownService {
    override def apply(input: CharSequence): Html =
      Html(new PegDownProcessor().markdownToHtml(input.toString))

  }
}