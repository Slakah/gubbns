import org.ektorp.http.{HttpClient, StdHttpClient}
import play.api.{Application, GlobalSettings}
import play.Logger

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")
    Logger.info("Yo dowg")


    val authenticatedHttpClient: HttpClient  = new StdHttpClient.Builder()
      .url("http://localhost:5984")
      .username("admin")
      .password("secret")
      .build()
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }
}
