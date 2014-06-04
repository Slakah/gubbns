package repositories

import scala.concurrent.Future
import services.CouchBlogServiceComponent
import models.Post
import play.api.libs.concurrent.Execution.Implicits._
import db.readers.View

import db.readers.ViewRead.viewReads
import db.ViewQuery
import play.api.libs.json.{Json, JsString}


trait PostRepositoryComponent {
  this: CouchBlogServiceComponent =>

  val postRepository: PostRepository

  trait PostRepository {
    def findByTitle(name: String): Future[Option[Post]]

    def getAll: Future[List[Post]]
  }


  class CouchPosts extends PostRepository {
    import models.PostFormat.postFormats

    override def findByTitle(rawTitle: String): Future[Option[Post]] = {
      val titleKey = Json.stringify(JsString(rawTitle))
      val byTitleRequest = couchBlog.postDesign.view("by_title", ViewQuery(key=Some(titleKey)))
      byTitleRequest.map(response =>
        response.json.as[View].rows.map(postRow => postRow.value.as[Post]).lift(0)
      )
    }


    override def getAll: Future[List[Post]] =
      couchBlog.postDesign.view("all").map(response =>
        response.json.as[View].rows.map(postRow => postRow.value.as[Post])
      )
  }

}
