package controllers

import play.api.mvc.{Action, Controller}
import models.PostRepository
import util.BlogDB

object Blog extends Controller {
  val postRepo = new PostRepository(BlogDB.db)

  def index = Action {
    val posts = postRepo.all
    Ok(views.html.blog_list.render(posts))
  }

  def title(name: String) = Action {
    val post = postRepo.byTitle(name)(0)

    Ok(views.html.blog_single.render(post))
  }

}