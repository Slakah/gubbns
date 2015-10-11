package controllers

import javax.inject.Inject

import akka.dispatch.Futures
import com.github.t3hnar.bcrypt._
import models.LoginForm
import org.joda.time.format.ISODateTimeFormat
import play.api.Play.current
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.{RequestHeader, Action, Controller}
import play.filters.csrf.CSRF.Token.getToken
import repositories.UserRepository

import scala.concurrent.Future


class Auth @Inject() (userRepository: UserRepository) extends Controller with Security {

  def login() = Action { implicit request =>
    Ok(views.html.user.login(Auth.loginForm))
  }

  val isoFormat = ISODateTimeFormat.dateTime

  def loginPost() = Action.async { implicit request =>
    Auth.loginForm.bindFromRequest.fold(
      formWithErrors =>
        Futures.successful(Unauthorized(views.html.user.login(formWithErrors))),
      validForm => {
        isValidLogin(validForm).map {
          case true => Redirect(routes.Home.index.url, ACCEPTED)
            .withSession(loginSession(validForm.email))
          case false =>
            val badForm = Auth.loginForm.fill(validForm).withGlobalError("Incorrect email or password")
            Unauthorized(views.html.user.login(badForm))
        }
      }
    )
  }

  private def isValidLogin(login: LoginForm): Future[Boolean] = {
    userRepository.fetchByEmail(login.email).map {
      case Some(user) =>
        login.password.isBcrypted(user.hashedPassword)
      case None => false
    }
  }
}

object Auth extends Controller {
  val loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> text(minLength = 3)
    )(LoginForm.apply)(LoginForm.unapply)
  )

  def unauthorisedLogin = { implicit request: RequestHeader =>
    Unauthorized(views.html.user.login(loginForm))
  }

}