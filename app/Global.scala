import global.BlogCouchSync
import play.api.{Application, GlobalSettings, Mode}
import scala.concurrent.Await
import scala.concurrent.duration._

object Global extends GlobalSettings {
  override def onStart(app: Application) {
    app.mode match {
      case Mode.Test =>
      case mode => Await.result(BlogCouchSync(app), Duration("60 seconds"))
    }
  }

  override def onStop(app: Application) {
  }
}
