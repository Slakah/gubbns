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

object Blog extends Controller {
  val postRepo = new PostRepository(BlogDB.db)

  def index = Action {
    val posts = postRepo.all
    Ok(views.html.index.render(posts))
  }

  def title(name: String) = Action {
    val post = postRepo.byTitle(name)

    Ok(views.html.index.render(Seq(post)))
  }

}