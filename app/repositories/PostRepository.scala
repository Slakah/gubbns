package repositories

import javax.inject.Inject

import com.google.inject.ImplementedBy
import db.ViewQuery
import db.models.View
import models.Post
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsObject, JsString, JsValue, Json}
import db.models.ViewFormat.viewFormats

import scala.concurrent.Future


@ImplementedBy(classOf[CouchPostRepository])
trait PostRepository {
  def findByTitle(name: String): Future[Option[Post]]

  def getAll: Future[List[Post]]

  def add(post: Post): Future[Unit]
}

import models.PostFormat.postFormats

class CouchPostRepository @Inject() (blogService: BlogCouchService) extends PostRepository {
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

  private def addPostTypeIdField(postJson: JsValue) = {
    postJson.as[JsObject] + ("typeId" -> JsString("post"))
  }

  def add(post: Post): Future[Unit] = {
    val postJson = addPostTypeIdField(Json.toJson(post))
    blogService.blogDb.addDoc(Json.prettyPrint(postJson)).map { response => () }
  }
}
