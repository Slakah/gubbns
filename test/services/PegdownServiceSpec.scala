package services

import org.specs2.mutable.Specification
import util.PegDownProcessor

class PegdownServiceSpec extends Specification with PegdownServiceComponent {
  val processor = new PegDownProcessor

  "MarkdownService" should {

    "transform # -> h1" in {
      processor("#test header").body must equalTo("<h1>test header</h1>")
    }

    "transform ## -> h2" in {
      processor("##test header").body must equalTo("<h2>test header</h2>")
    }

    "transform ### -> h3" in {
      processor("###test header").body must equalTo("<h3>test header</h3>")
    }

    "transform #### -> h4" in {
      processor("####test header").body must equalTo("<h4>test header</h4>")
    }

    "handle links" in {
      processor("[here](gubbns.com)").body must contain("""<a href="gubbns.com">here</a>""")
    }
  }
}
