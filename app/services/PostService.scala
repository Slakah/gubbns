package services

import javax.inject.Inject

import com.google.inject.ImplementedBy
import models.{DisplayPost, Post}
import util.MarkdownProcessor
import repositories.PostRepository
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@ImplementedBy(classOf[Posts])
trait PostService {
  def findByTitle(name: String): Future[Option[Post]]

  def getAll: Future[List[Post]]

  def add(post: Post): Future[Unit]
}

class Posts @Inject() (markdown: MarkdownProcessor, postRepository: PostRepository) extends PostService {
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

