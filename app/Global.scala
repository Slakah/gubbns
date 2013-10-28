import play.api.{Application, GlobalSettings}
import play.Logger

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")
    Logger.info("Yo dowg")
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }
}
