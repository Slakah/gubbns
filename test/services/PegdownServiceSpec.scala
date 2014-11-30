package services

import org.specs2.mutable.Specification

class PegdownServiceSpec extends Specification with PegdownServiceComponent {
  "MarkdownService" should {

    "transform # -> h1" in {
      markdown("#test header").body must equalTo("<h1>test header</h1>")
    }

    "transform ## -> h2" in {
      markdown("##test header").body must equalTo("<h2>test header</h2>")

    }

    "transform ### -> h3" in {
      markdown("###test header").body must equalTo("<h3>test header</h3>")

    }

    "transform #### -> h4" in {
      markdown("####test header").body must equalTo("<h4>test header</h4>")

    }

    "handle links" in {
      markdown("[here](gubbns.com)").body must contain("""<a href="gubbns.com">here</a>""")

    }
  }
}
