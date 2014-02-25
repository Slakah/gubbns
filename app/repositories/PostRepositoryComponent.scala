package repositories

import scala.concurrent.Future
import services.CouchBlogServiceComponent
import models.Post
import models.PostFormat.postFormats
import play.api.libs.concurrent.Execution.Implicits._
import db.readers.View


trait PostRepositoryComponent {
  this: CouchBlogServiceComponent =>

  val postRepository: PostRepository

  trait PostRepository {
    def findByTitle(name: String): Future[Option[Post]]

    def getAll: Future[List[Post]]
  }


  class CouchPosts extends PostRepository {

    // TODO Change to use the set up couchdb view function
    override def findByTitle(name: String): Future[Option[Post]] =
      getAll.map(allPosts =>
        allPosts.find(_.title.equalsIgnoreCase(name.replace("-", " ")))
      )


    override def getAll: Future[List[Post]] =
      couchBlog.postDesign.view("all").map(response =>
        response.json.as[View]).map(view =>
        view.rows.map(postRow => postRow.value.as[Post]
        )
        )

  }

}
