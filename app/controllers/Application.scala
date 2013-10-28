package controllers

import play.api.mvc.{Action, Controller}
import models.{Post}
import org.joda.time.DateTime

object Application extends Controller {
  def index = Action {
    Ok(views.html.index.render(Seq(Post("title","content", DateTime.now, "James"),
      Post("title2","content2", DateTime.now, "James"))))
  }
}