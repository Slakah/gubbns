package controllers

import play.api.Play.current
import akka.dispatch.Futures
import com.github.t3hnar.bcrypt._
import models.LoginForm
import org.joda.time.format.ISODateTimeFormat
import play.api.data.Forms._
import play.api.data._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.{Action, Controller, RequestHeader}
import play.filters.csrf.CSRF.Token.getToken
import repositories.UserRepositoryComponent
import play.api.i18n.Messages.Implicits._

import scala.concurrent.Future

object Auth extends AuthImpl with Application

trait AuthImpl extends Controller
    with UserRepositoryComponent with Security {
  val loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> text(minLength = 3)
    )(LoginForm.apply)(LoginForm.unapply)
  )

  def login() = Action { implicit request =>
    Ok(views.html.user.login(loginForm))
  }

  def unauthorisedLogin = { implicit request: RequestHeader =>
    Unauthorized(views.html.user.login(loginForm))
  }

  val isoFormat = ISODateTimeFormat.dateTime

  def loginPost() = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors =>
        Futures.successful(Unauthorized(views.html.user.login(formWithErrors))),
      validForm => {
        isValidLogin(validForm).map {
          case true => Redirect(routes.Home.index.url, ACCEPTED)
            .withSession(loginSession(validForm.email))
          case false =>
            val badForm = loginForm.fill(validForm).withGlobalError("Incorrect email or password")
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

