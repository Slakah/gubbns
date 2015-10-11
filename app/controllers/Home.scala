package controllers

import javax.inject.Inject

import models.DisplayPost
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.{Controller, Action}
import services.PostService
import util.PegDownProcessor

class Home @Inject() (posts: PostService) extends Controller {
  def index = Action.async {
    posts.getAll.map { allPosts =>
      val allDisplayPosts = allPosts.map(DisplayPost(_)(new PegDownProcessor))
      Ok(views.html.home.render(allDisplayPosts))
    }
  }
}
