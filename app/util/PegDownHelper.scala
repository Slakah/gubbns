package util

import com.google.inject.ImplementedBy
import play.twirl.api.Html

@ImplementedBy(classOf[PegDownProcessor])
trait MarkdownProcessor {
  def apply(input: CharSequence): Html
}

class PegDownProcessor extends MarkdownProcessor {
  lazy val pegDownProcessor = new org.pegdown.PegDownProcessor()

  override def apply(input: CharSequence): Html = processMarkdown(input)

  def processMarkdown(input: CharSequence): Html =
    Html(pegDownProcessor.markdownToHtml(input.toString))

}
