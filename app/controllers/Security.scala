package controllers

import play.api.http.Status
import play.api.mvc._

trait Security extends Status {

  def email(request: RequestHeader) = request.session.get("email")

  def onUnauthorized(request: RequestHeader) =
    Results.Redirect(routes.Auth.login.url, UNAUTHORIZED)

  def withAuth(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(email, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
  }
}
