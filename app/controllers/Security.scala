package controllers

import play.api.http.Status
import play.api.mvc.Security.AuthenticatedBuilder
import play.api.mvc._

trait Security extends Status {

  def email(request: RequestHeader) = request.session.get("email")

  object Authenticated
    extends AuthenticatedBuilder[String](request => email(request), request => Auth.unauthorisedLogin(request))

}

