package services

import play.twirl.api.Html
import util.PegDownProcessor

trait MarkdownService {
  def apply(input: CharSequence): Html
}

trait MarkdownServiceComponent {
  def markdown: MarkdownService
}

trait PegdownServiceComponent extends MarkdownServiceComponent {
  override implicit val markdown: MarkdownService = PegdownService

  object PegdownService extends MarkdownService {
    val processor = new PegDownProcessor

    override def apply(input: CharSequence): Html = processor(input)

  }
}