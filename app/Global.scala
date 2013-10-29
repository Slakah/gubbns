import org.ektorp.{CouchDbConnector, CouchDbInstance}
import org.ektorp.http.{HttpClient, StdHttpClient}
import org.ektorp.impl.StdCouchDbInstance
import play.api.{Application, GlobalSettings}
import play.Logger

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")

    val httpClient: HttpClient  = new StdHttpClient.Builder()
      .url("http://localhost:5984")
      .build()
    Logger.info("Hmmpppf")
    val dbInstance: CouchDbInstance = new StdCouchDbInstance(httpClient)
    Logger.info("Hmmpppf2")

    val db: CouchDbConnector = dbInstance.createConnector("blog", false)

    Logger.info("Hmmpppf3")

  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }
}
