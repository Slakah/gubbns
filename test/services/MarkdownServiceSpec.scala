package services

import org.specs2.mutable.Specification
import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration
import ExecutionContext.Implicits.global


class MarkdownServiceSpec extends Specification with MarkdownServiceComponent with PegdownServiceComponent {
  "MarkdownService" should {

    def testMarkdown(input: String) = {
      markdown(input).map(_.body)
    }

    "transform # -> h1" in {
      markdown("#test header").map(_.body) must equalTo("<h1>test header</h1>").await
    }

    "transform ## -> h2" in {
      markdown("##test header").map(_.body) must equalTo("<h2>test header</h2>").await
    }

    "transform ### -> h3" in {
      markdown("###test header").map(_.body) must equalTo("<h3>test header</h3>").await
    }

    "transform #### -> h4" in {
      markdown("####test header").map(_.body) must equalTo("<h4>test header</h4>").await
    }

    "handle links" in {
      markdown("[here](gubbns.com)").map(_.body) must contain("""<a href="gubbns.com">here</a>""").await
    }
  }
}