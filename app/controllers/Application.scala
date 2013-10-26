package controllers

import play.api.mvc.{Action, Controller}
import models.{BlogPostFixture, BlogPost}

object Application extends Controller {
  def index = Action {
    Ok(views.html.index.render(Seq(BlogPostFixture, BlogPostFixture)))
  }
}