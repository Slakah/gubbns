package controllers

import models.User
import org.specs2.mock.Mockito
import org.specs2.mutable._
import com.github.t3hnar.bcrypt._
import play.api.mvc.Request
import play.api.test.{WithApplication, FakeRequest}
import play.api.test.Helpers._

import scala.concurrent.Future

class AuthSpec extends Specification with Mockito {
  val email = "joe.bloggs@email.com"
  val password = "testpassword"
  val hashedPassword = password.bcrypt
  val user = User(email, hashedPassword)
  val futureUser = Future.successful(Some(user))

  val singleUserAuth = mock[AuthImpl]
  singleUserAuth.userRepository.fetchByEmail(email) returns futureUser


  "Auth" should {
    "allow login" in new WithApplication {
      val loginRequest = FakeRequest(POST, "/")
        .withFormUrlEncodedBody(
          ("email", email),
          ("password", password))

      val loginResponse = singleUserAuth.loginPost()(loginRequest)
      status(loginResponse) must equalTo(ACCEPTED)
    }
  }
}
