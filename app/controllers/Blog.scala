package controllers

import play.api.mvc.Action
import play.api.libs.concurrent.Execution.Implicits._
import scala.None
import models.DisplayPost
import scala.concurrent.Future

object Blog extends Application {
  def index = Action.async {
    posts.getAll.flatMap{allPosts =>
      Future.sequence(allPosts.map(DisplayPost.apply)).map {
        displayPosts =>
          Ok(views.html.blog.list.render(displayPosts))
      }
    }
  }

  def title(title: String) = Action.async {
    posts.findByTitle(title).flatMap {
      case Some(matchingPost) => DisplayPost(matchingPost).map(displayPost => Ok(views.html.blog.single(displayPost, None, None)))
      case None => Future.successful(NotFound)
    }
  }
}