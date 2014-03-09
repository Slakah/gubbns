import global.BlogCouchSync
import play.api.{Application, GlobalSettings}
import play.Logger


object Global extends GlobalSettings {
  override def onStart(app: Application) {
    BlogCouchSync().onStart(app)
  }

  override def onStop(app: Application) {
  }
}