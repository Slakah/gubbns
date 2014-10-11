package controllers

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.http.Status
import play.api.mvc.Security.AuthenticatedBuilder
import play.api.mvc._

trait Security extends Status {
  private val isoFormat = ISODateTimeFormat.dateTime

  def loginSession(email: String) = Session(
    Map("email" -> email,
        "login-time" -> isoFormat.print(DateTime.now))
  )

  private def email(request: RequestHeader) = request.session.get("email")

  object Authenticated
    extends AuthenticatedBuilder[String](email, Auth.unauthorisedLogin)

}

