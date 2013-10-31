package controllers

import play.api.mvc.{Action, Controller}
import models.{Post, PostRepository}
import org.ektorp.http.{StdHttpClient, HttpClient}
import org.ektorp.{CouchDbConnector, CouchDbInstance}
import org.ektorp.impl.{StdObjectMapperFactory, StdCouchDbInstance}
import scala.collection.JavaConverters._
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import play.Logger
import util.BlogDB
import org.joda.time.DateTime

object Application extends Controller {
  def index = Action {
    val posts: Seq[Post] = asScalaBufferConverter(new PostRepository(BlogDB.db).getAll()).asScala.toList
    Ok(views.html.index.render(posts))
  }
}