package controllers

import akka.dispatch.Futures
import org.joda.time.DateTime
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Security.AuthenticatedRequest
import play.api.mvc.{RequestHeader, Controller, Action}
import play.api.libs.concurrent.Execution.Implicits._
import models.{Post, DisplayPost}
import services.{MarkdownServiceComponent, PostServiceComponent}
import scala.concurrent.Future

object Blog extends BlogImpl with Application

trait BlogImpl extends Controller with Security
    with PostServiceComponent with MarkdownServiceComponent {
  def index = Action.async {
    posts.getAll.flatMap{allPosts =>
      Future.sequence(allPosts.map(DisplayPost(_)(markdown))).map {
        displayPosts =>
          Ok(views.html.blog.list.render(displayPosts))
      }
    }
  }

  def title(title: String) = Action.async {
    posts.findByTitle(title).flatMap {
      case Some(matchingPost) => DisplayPost(matchingPost)(markdown).map(displayPost => Ok(views.html.blog.single(displayPost, None, None)))
      case None => Future.successful(NotFound)
    }
  }

  case class PostForm(title: String, content: String) {
    def toPost[A](implicit request: AuthenticatedRequest[A, String]): Post = {
      Post(title = title, content = content, published = DateTime.now, author = request.user)
    }
  }

  val postForm = Form(
    mapping(
      "title" -> text(minLength = 3),
      "content" -> text(minLength = 20)
    )(PostForm.apply)(PostForm.unapply))


  def addPost() = Authenticated.async { implicit request =>
    postForm.bindFromRequest.fold(
      formWithErrors => Futures.successful(BadRequest),
      validPostForm => {
        posts.add(validPostForm.toPost).map {_ => Created}
      }
    )
  }
}