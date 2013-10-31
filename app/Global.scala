import org.ektorp.{CouchDbConnector, CouchDbInstance}
import org.ektorp.http.{HttpClient, StdHttpClient}
import org.ektorp.impl.StdCouchDbInstance
import play.api.{Application, GlobalSettings}
import play.Logger


object Global extends GlobalSettings {



  override def onStart(app: Application) {
    Logger.info("Application has started")

  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }
}