package controllers

import com.github.t3hnar.bcrypt._
import models.User
import org.specs2.mock.Mockito
import org.specs2.mutable._
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}
import play.filters.csrf.CSRF
import repositories.UserRepository

import scala.concurrent.Future

class AuthSpec extends Specification with Mockito {
  val email = "joe.bloggs@email.com"
  val password = "testpassword"
  val hashedPassword = password.bcrypt
  val user = User(email, hashedPassword)
  val futureUser = Future.successful(Some(user))


  private def fakeLoginRequest(email: String = email,
                               password:String = password) = {
    FakeRequest(POST, routes.Auth.login().url)
      .withFormUrlEncodedBody(
        ("email", email),
        ("password", password))
      .withSession(("csrfToken", CSRF.SignedTokenProvider.generateToken))
  }

  val singleUserRepository = mock[UserRepository]
  singleUserRepository.fetchByEmail(email) returns futureUser

  val auth = new Auth(singleUserRepository)

  "Auth login" should {
    "allow login" in new WithApplication {
      val loginResponse = auth.loginPost()(fakeLoginRequest())
      status(loginResponse) must equalTo(ACCEPTED)
      session(loginResponse).get("email") must beSome(email)
    }
    "be unauthorised for incorrect password" in new WithApplication {
      val incorrectPasswordRequest = fakeLoginRequest(password = "incorrectpassword")

      val loginResponse = auth.loginPost()(incorrectPasswordRequest)
      status(loginResponse) must equalTo(UNAUTHORIZED)
      session(loginResponse).get("email") must beNone
    }

    val unknownEmail = "unknown@email.com"
    singleUserRepository.fetchByEmail(unknownEmail) returns Future.successful(None)

    "be unauthorised for unknown email" in new WithApplication {
      val unknownEmailRequest = fakeLoginRequest(email = unknownEmail)

      val loginResponse = auth.loginPost()(unknownEmailRequest)
      status(loginResponse) must equalTo(UNAUTHORIZED)
      session(loginResponse).get("email") must beNone
    }
  }
}
