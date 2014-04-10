package controllers

import play.api.mvc.Action
import play.api.libs.concurrent.Execution.Implicits._
import models.DisplayPost
import scala.concurrent.Future


object Home extends Application {
  def index = Action.async {
    posts.getAll.flatMap{allPosts =>
      Future.sequence(allPosts.map(DisplayPost.apply)).map {
        displayPosts =>
          Ok(views.html.blog.list.render(displayPosts))
      }
    }
  }
}
