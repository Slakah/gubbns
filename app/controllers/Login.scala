package controllers

import models.User
import play.api.mvc.Action
import play.api.data._
import play.api.data.Forms._

object Login extends Application {
  val userForm = Form(
    mapping(
      "email" -> email,
      "password" -> text(minLength = 3)
    )(User.apply)(User.unapply)
  )

  def login() = Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.login.login(formWithErrors))
      },
      userData => {
        Redirect(routes.Home.index)
      }
    )
  }
}
