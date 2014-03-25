import org.pegdown.PegDownProcessor
import play.api.templates.Html

object Markdown {
  def apply(input: CharSequence) = Html(new PegDownProcessor().markdownToHtml(input.toString))
}
