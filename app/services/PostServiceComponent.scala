package services

import repositories.PostRepositoryComponent
import models.Post
import scala.concurrent.Future


trait PostServiceComponent {
  this: PostRepositoryComponent =>

  val posts: PostService

  class PostService {
    def findByTitle(name: String): Future[Option[Post]] = postRepository.findByTitle(name)

    def getAll: Future[List[Post]] = postRepository.getAll
  }

}
