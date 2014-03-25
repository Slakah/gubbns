package controllers

import play.api.mvc.Action
import play.api.libs.concurrent.Execution.Implicits._


object Home extends Application {
  def index = Action.async {
    posts.getAll.map(allPosts =>
      Ok(views.html.home.render(allPosts))
    )
  }
}
