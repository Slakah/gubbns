package repositories

import db.ViewQuery
import db.readers.View
import models.Post
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsString, Json}

import scala.concurrent.Future

trait PostRepository {
  def findByTitle(name: String): Future[Option[Post]]

  def getAll: Future[List[Post]]
}

trait PostRepositoryComponent {
  val postRepository: PostRepository
}

import db.DatabaseName._
import models.PostFormat.postFormats
import db.readers.ViewRead.viewReads

trait CouchPostRepositoryComponent extends PostRepositoryComponent {
  this: CouchServiceComponent =>

  val postRepository: PostRepository = CouchPostRepository

  object CouchPostRepository extends PostRepository {
    private val db = couchService.couch.database("blog".asDatabaseName)
    private val postDesign = db.databaseDesign("post")

    override def findByTitle(rawTitle: String): Future[Option[Post]] = {
      val titleKey = Json.stringify(JsString(rawTitle))
      val byTitleRequest = postDesign.view("by_title", ViewQuery(key = Some(titleKey)))
      byTitleRequest.map {
        case Right(response) => response.json.as[View].rows.map(postRow => postRow.value.as[Post]).lift(0)
      }
    }

    override def getAll: Future[List[Post]] =
      postDesign.view("all").map {
        case Right(response) => response.json.as[View].rows.map(postRow => postRow.value.as[Post])
      }
  }
}
