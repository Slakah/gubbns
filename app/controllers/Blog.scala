package controllers

import play.api.Play.current
import akka.dispatch.Futures
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Controller, Action}
import play.api.libs.concurrent.Execution.Implicits._
import models.{PostForm, DisplayPost}
import services.{MarkdownServiceComponent, PostServiceComponent}
import scala.concurrent.Future
import play.api.i18n.Messages.Implicits._


object Blog extends BlogImpl with Application

trait BlogImpl extends Controller with Security
    with PostServiceComponent with MarkdownServiceComponent {

  def index = Action.async {
    posts.getAll.map { allPosts =>
      val allDisplayPosts = allPosts.map(DisplayPost(_)(markdown))
      Ok(views.html.blog.list.render(allDisplayPosts))
    }
  }

  def title(title: String) = Action.async {
    posts.findByTitle(title).map {
      case Some(matchingPost) =>
        Ok(views.html.blog.single(DisplayPost(matchingPost)(markdown), None, None))
      case None => NotFound
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
        posts.add(validPostForm.toPost).map {_ =>
          Redirect(routes.Blog.index.url, CREATED)
        }
      }
    )
  }

  def addPostForm() = Action { implicit request =>
    Ok(views.html.blog.add(postForm))
  }

}