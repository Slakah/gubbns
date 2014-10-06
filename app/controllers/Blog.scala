package controllers

import play.api.mvc.{Controller, Action}
import play.api.libs.concurrent.Execution.Implicits._
import models.DisplayPost
import services.{MarkdownServiceComponent, PostServiceComponent}
import scala.concurrent.Future

object Blog extends BlogImpl with Application

trait BlogImpl extends Controller
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
}