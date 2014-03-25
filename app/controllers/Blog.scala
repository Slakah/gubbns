package controllers

import play.api.mvc.Action
import play.api.libs.concurrent.Execution.Implicits._
import scala.None

object Blog extends Application {

  def index = longCache("blogAll") {
    Action.async {
      posts.getAll.map(allPosts =>
        Ok(views.html.blog.list.render(allPosts))
      )
    }
  }

  def title(title: String) = longCache(s"blog.$title") {
    Action.async {
      posts.findByTitle(title).map {
        case Some(matchingPost) => Ok(views.html.blog.single.render(matchingPost, None, None))
        case None => NotFound
      }
    }
  }

}