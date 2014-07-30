package repositories

import db.ViewQuery
import db.models.View
import db.models.ViewFormat.viewFormats
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

import models.PostFormat.postFormats

trait CouchPostRepositoryComponent extends PostRepositoryComponent {
  this: BlogCouchServiceComponent =>

  val postRepository: PostRepository = CouchPostRepository

  object CouchPostRepository extends PostRepository {
    private val db = blogService.blogDb
    private val postDesign = blogService.postDesign

    override def findByTitle(rawTitle: String): Future[Option[Post]] = {
      val titleKey = Json.stringify(JsString(rawTitle))
      val byTitleRequest = postDesign.view("by_title", ViewQuery(key = Some(titleKey)))
      byTitleRequest.map {
        response => response.json.as[View].rows.map(postRow => postRow.value.as[Post]).lift(0)
      }
    }

    override def getAll: Future[List[Post]] =
      postDesign.view("all").map {
        response => response.json.as[View].rows.map(postRow => postRow.value.as[Post])
      }
  }
}
