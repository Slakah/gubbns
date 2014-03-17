package controllers

import play.api.mvc.Action
import play.api.libs.concurrent.Execution.Implicits._
import scala.None
import play.api.Logger


object Blog extends Application {

  def index = Action.async {
    posts.getAll.map(allPosts =>
      Ok(views.html.blog.list.render(allPosts))
    )
  }

  def title(title: String) = Action.async {
    posts.findByTitle(title).map(_ match {
      case Some(matchingPost) => Ok(views.html.blog.single.render(matchingPost, None, None))
      case None => NotFound
    })
  }

}