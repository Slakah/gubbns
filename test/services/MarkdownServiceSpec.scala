package services

import org.specs2.mutable.Specification

class MarkdownServiceSpec extends Specification with PegdownServiceComponent {
  "MarkdownService" should {

    "transform # -> h1" in {
      markdown("#test header").map(_.body) must equalTo("<h1>test header</h1>").await(retries = 1)
    }

    "transform ## -> h2" in {
      markdown("##test header").map(_.body) must equalTo("<h2>test header</h2>").await(retries = 1)

    }

    "transform ### -> h3" in {
      markdown("###test header").map(_.body) must equalTo("<h3>test header</h3>").await(retries = 1)

    }

    "transform #### -> h4" in {
      markdown("####test header").map(_.body) must equalTo("<h4>test header</h4>").await(retries = 1)

    }

    "handle links" in {
      markdown("[here](gubbns.com)").map(_.body) must contain("""<a href="gubbns.com">here</a>""").await(retries = 1)

    }
  }
}
