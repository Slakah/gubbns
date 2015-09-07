import play.api.{Application, GlobalSettings}
import play.api.mvc.WithFilters
import play.filters.csrf.CSRFFilter
import plugins.BlogCouchSync

import scala.concurrent.Await
import scala.concurrent.duration._


object Global extends WithFilters(CSRFFilter()) with GlobalSettings {
  override def onStart(app: Application) = {
    Await.ready(BlogCouchSync.sync(), 5.seconds)
    super.onStart(app)
  }
}