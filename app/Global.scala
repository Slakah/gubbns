import global.BlogCouchSync
import play.api.{Application, GlobalSettings}
import scala.concurrent.Await
import scala.concurrent.duration._

object Global extends GlobalSettings {
  override def onStart(app: Application) {
    Await.result(BlogCouchSync(app), Duration("60 seconds"))
  }

  override def onStop(app: Application) {
  }
}