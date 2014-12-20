package services

import models.{DisplayPost, Post}
import repositories.PostRepositoryComponent
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait PostService {
  def findByTitle(name: String): Future[Option[Post]]

  def getAll: Future[List[Post]]

  def add(post: Post): Future[Unit]
}

trait PostServiceComponent {
  def posts: PostService
}

trait PostsComponent extends PostServiceComponent {
  this: PostRepositoryComponent with MarkdownServiceComponent =>

  override val posts = Posts

  object Posts extends PostService {
    def findByTitleDisplay(name: String): Future[Option[DisplayPost]] = {
      findByTitle(name).map { post => post.map(DisplayPost(_)(markdown)) }
    }

    def findByTitle(name: String): Future[Option[Post]] = postRepository.findByTitle(name)

    def getAll: Future[List[Post]] = postRepository.getAll

    def getAllDisplay: Future[List[DisplayPost]] = {
      getAll.map { post => post.map(DisplayPost(_)(markdown))}
    }

    def add(post: Post): Future[Unit] = postRepository.add(post)
  }
}

