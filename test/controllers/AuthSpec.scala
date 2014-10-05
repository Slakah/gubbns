package controllers

import models.User
import org.specs2.mock.Mockito
import org.specs2.mutable._
import com.github.t3hnar.bcrypt._
import play.api.test.{WithApplication, FakeRequest}
import play.api.test.Helpers._
import repositories.UserRepository

import scala.concurrent.Future

class AuthSpec extends Specification with Mockito {
  val email = "joe.bloggs@email.com"
  val password = "testpassword"
  val hashedPassword = password.bcrypt
  val user = User(email, hashedPassword)
  val futureUser = Future.successful(Some(user))

  val singleUserRepository = mock[UserRepository]
  singleUserRepository.fetchByEmail(email) returns futureUser

  object SingleAuth extends AuthImpl {
    override def userRepository: UserRepository = singleUserRepository
  }

  "Auth" should {
    "allow login" in new WithApplication {
      val loginRequest = FakeRequest(POST, "/")
        .withFormUrlEncodedBody(
          ("email", email),
          ("password", password))

      val loginResponse = SingleAuth.loginPost()(loginRequest)
      status(loginResponse) must equalTo(ACCEPTED)
      session(loginResponse).get("email") must beSome(email)
    }
    "be unauthorised for incorrect password" in new WithApplication {
      val loginRequest = FakeRequest(POST, "/")
        .withFormUrlEncodedBody(
          ("email", email),
          ("password", "incorrectpassword"))

      val loginResponse = SingleAuth.loginPost()(loginRequest)
      status(loginResponse) must equalTo(UNAUTHORIZED)
      session(loginResponse).get("email") must beNone
    }

    val unknownEmail = "unknown@email.com"
    singleUserRepository.fetchByEmail(unknownEmail) returns Future.successful(None)

    "be unauthorised for unknown email" in new WithApplication {
      val loginRequest = FakeRequest(POST, "/")
        .withFormUrlEncodedBody(
          ("email", unknownEmail),
          ("password", password))

      val loginResponse = SingleAuth.loginPost()(loginRequest)
      status(loginResponse) must equalTo(UNAUTHORIZED)
      session(loginResponse).get("email") must beNone
    }
  }
}
