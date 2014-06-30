package services

import repositories.PostRepositoryComponent
import models.Post
import scala.concurrent.Future


trait PostServiceComponent {
  val posts: PostService

  trait PostService {
    def findByTitle(name: String): Future[Option[Post]]

    def getAll: Future[List[Post]]
  }

  class Posts extends PostService {
    this: PostRepositoryComponent =>

    def findByTitle(name: String): Future[Option[Post]] = postRepository.findByTitle(name)

    def getAll: Future[List[Post]] = postRepository.getAll
  }

}
