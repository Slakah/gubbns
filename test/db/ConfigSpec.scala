package db

import org.specs2.mutable._
import play.api.Configuration
import play.api.test.Helpers._
import play.api.test._

class FakeApplicationWithEmptyConfig extends FakeApplication {
  override def configuration: Configuration = Configuration.empty
}

class ConfigSpec extends Specification {
  "Config" should {
    "use default host 'localhost'" in {
      running(new FakeApplicationWithEmptyConfig) {
        val playConfig = new PlayConfigService
        playConfig.config().host must equalTo("localhost")
      }
    }

    "use configuration host of 'http://gubbns.com'" in {
      val host = "http://gubbns.com"
      running(FakeApplication(additionalConfiguration =
        Map("couchdb.default.host" -> host))) {
        val playConfig = new PlayConfigService
        playConfig.config().host must equalTo(host)
      }
    }

    "use default port 5984" in {
      running(new FakeApplicationWithEmptyConfig) {
        val playConfig = new PlayConfigService
        playConfig.config().port must equalTo(5984)
      }
    }

    "use configuration port 24" in {
      val port = 24
      running(FakeApplication(additionalConfiguration =
        Map("couchdb.default.port" -> port))) {
        val playConfig = new PlayConfigService
        playConfig.config().port must equalTo(port)
      }
    }
  }
}
