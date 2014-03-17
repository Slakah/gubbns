package repositories

import scala.concurrent.Future
import services.CouchBlogServiceComponent
import models.Post
import models.PostFormat.postFormats
import play.api.libs.concurrent.Execution.Implicits._
import db.readers.View

import db.readers.ViewRead.viewReads
import db.ViewQuery
import play.api.Logger


trait PostRepositoryComponent {
  this: CouchBlogServiceComponent =>

  val postRepository: PostRepository

  trait PostRepository {
    def findByTitle(name: String): Future[Option[Post]]

    def getAll: Future[List[Post]]
  }


  class CouchPosts extends PostRepository {
    import util.Slugify.slugify

    override def findByTitle(rawTitle: String): Future[Option[Post]] = {
      val slugTitle = slugify(rawTitle)
      couchBlog.postDesign.view("by_title", ViewQuery(key=Some(s""""$slugTitle""""))).map(response =>
        response.json.as[View].rows.map(postRow => postRow.value.as[Post]).lift(0)
      )
    }


    override def getAll: Future[List[Post]] =
      couchBlog.postDesign.view("all").map(response =>
        response.json.as[View].rows.map(postRow => postRow.value.as[Post])
      )
  }

}
