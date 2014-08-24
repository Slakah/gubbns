package controllers

import controllers.Login._
import play.api.mvc.Action

object Logout {

  def logoutPost() = Action {
    Redirect(routes.Home.index).withNewSession
  }
}
