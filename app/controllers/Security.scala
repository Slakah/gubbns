package controllers

import play.api.http.Status
import play.api.mvc.Security.AuthenticatedBuilder
import play.api.mvc._

trait Security extends Status {

  private def email(request: RequestHeader) = request.session.get("email")

  object Authenticated
    extends AuthenticatedBuilder[String](email, Auth.unauthorisedLogin)

}

