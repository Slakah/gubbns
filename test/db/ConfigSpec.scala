package db

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.Configuration
import plugins.BlogCouchSyncPlugin

class FakeApplicationWithEmptyConfig extends FakeApplication(withoutPlugins = Seq(classOf[BlogCouchSyncPlugin].getName)) {
  override def configuration: Configuration = Configuration.empty
}

class ConfigSpec extends Specification with PlayConfigService {
  "Config" should {
    "use default host 'localhost'" in {
      running(new FakeApplicationWithEmptyConfig) {
        config().host must equalTo("localhost")
      }
    }

    "use configuration host of 'http://gubbns.com'" in {
      val host = "http://gubbns.com"
      running(FakeApplication(additionalConfiguration =
        Map("couchdb.default.host" -> host))) {
        config().host must equalTo(host)
      }
    }

    "use default port 5984" in {
      running(new FakeApplicationWithEmptyConfig){
        config().port must equalTo(5984)
      }
    }

    "use configuration port 24" in {
      val port = 24
      running(FakeApplication(additionalConfiguration =
        Map("couchdb.default.port" -> port))) {
        config().port must equalTo(port)
      }
    }
  }
}
