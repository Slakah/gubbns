package services

import models.Post
import repositories.PostRepositoryComponent

import scala.concurrent.Future

trait PostService {
  def findByTitle(name: String): Future[Option[Post]]

  def getAll: Future[List[Post]]
}

trait PostServiceComponent {
  def posts: PostService
}

trait PostsComponent extends PostServiceComponent {
  this: PostRepositoryComponent =>

  override val posts = Posts

  object Posts extends PostService {
    def findByTitle(name: String): Future[Option[Post]] = postRepository.findByTitle(name)

    def getAll: Future[List[Post]] = postRepository.getAll
  }
}

