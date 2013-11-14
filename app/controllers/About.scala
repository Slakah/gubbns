package controllers

import play.api.mvc.{Action, Controller}

object About extends Controller {
  def index = Action {
    Ok(views.html.about.render)
  }

}
