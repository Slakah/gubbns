package controllers

import akka.dispatch.Futures
import models.LoginForm
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.mvc.{Controller, Action}
import play.api.data._
import play.api.data.Forms._
import com.github.t3hnar.bcrypt._
import play.api.libs.concurrent.Execution.Implicits._
import repositories.UserRepositoryComponent

import scala.concurrent.Future

object Auth extends AuthImpl with Application

trait AuthImpl extends Controller with UserRepositoryComponent {
  val loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> text(minLength = 3)
    )(LoginForm.apply)(LoginForm.unapply)
  )

  def login() = Action {
    Ok(views.html.user.login(loginForm))
  }

  def unauthorisedLogin =
    Unauthorized(views.html.user.login(loginForm))

  val isoFormat = ISODateTimeFormat.dateTime

  def loginPost() = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors =>
        Futures.successful(Unauthorized(views.html.user.login(formWithErrors))),
      validForm => {
        isValidLogin(validForm).map {
          case true => Redirect(routes.Home.index.url, ACCEPTED).withSession(
            "email" -> validForm.email,
            "login-time" -> isoFormat.print(DateTime.now)
          )
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

  def logoutPost() = Action {
    Redirect(routes.Home.index).withNewSession
  }
}

