package controllers

import play.api.mvc.{Controller, Action}
import play.api.libs.concurrent.Execution.Implicits._
import models.DisplayPost
import services.{MarkdownServiceComponent, PostServiceComponent}
import scala.concurrent.Future

object Home extends HomeImpl with Application

trait HomeImpl extends Controller
    with PostServiceComponent with MarkdownServiceComponent  {
  def index = Action.async {
    posts.getAll.map { allPosts =>
      val allDisplayPosts = allPosts.map(DisplayPost(_)(markdown))
      Ok(views.html.home.render(allDisplayPosts))
    }
  }
}

