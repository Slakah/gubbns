package controllers

import models.User
import org.specs2.mock.Mockito
import org.specs2.mutable._
import com.github.t3hnar.bcrypt._
import play.api.test.{WithApplication, FakeRequest}
import play.api.test.Helpers._
import play.filters.csrf.{CSRFConfig, CSRF}
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

  object SingleAuth extends AuthImpl {
    override val userRepository: UserRepository = singleUserRepository
  }

  "Auth login" should {
    "allow login" in new WithApplication {
      val loginResponse = SingleAuth.loginPost()(fakeLoginRequest())
      status(loginResponse) must equalTo(ACCEPTED)
      session(loginResponse).get("email") must beSome(email)
    }
    "be unauthorised for incorrect password" in new WithApplication {
      val incorrectPasswordRequest = fakeLoginRequest(password = "incorrectpassword")

      val loginResponse = SingleAuth.loginPost()(incorrectPasswordRequest)
      status(loginResponse) must equalTo(UNAUTHORIZED)
      session(loginResponse).get("email") must beNone
    }

    val unknownEmail = "unknown@email.com"
    singleUserRepository.fetchByEmail(unknownEmail) returns Future.successful(None)

    "be unauthorised for unknown email" in new WithApplication {
      val unknownEmailRequest = fakeLoginRequest(email = unknownEmail)

      val loginResponse = SingleAuth.loginPost()(unknownEmailRequest)
      status(loginResponse) must equalTo(UNAUTHORIZED)
      session(loginResponse).get("email") must beNone
    }
  }
}
