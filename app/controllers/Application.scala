package controllers

import play.api.mvc.{Action, Controller}
import models.{Post, PostRepository}
import org.ektorp.http.{StdHttpClient, HttpClient}
import org.ektorp.{CouchDbConnector, CouchDbInstance}
import org.ektorp.impl.StdCouchDbInstance
import scala.collection.JavaConverters._

object Application extends Controller {
  val httpClient: HttpClient  = new StdHttpClient.Builder()
    .url("http://localhost:5984")
    .build()
  val dbInstance: CouchDbInstance = new StdCouchDbInstance(httpClient)
  def db: CouchDbConnector = dbInstance.createConnector("blog", false)


  def index = Action {
    val posts: Seq[Post] = asScalaBufferConverter(new PostRepository(db) getAll()).asScala.toList
    Ok(views.html.index.render(posts))
  }
}