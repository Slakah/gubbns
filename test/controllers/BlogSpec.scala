package controllers

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}

class BlogSpec extends Specification with Mockito {

  "Blog" should {
    "allow adding of posts" in {
      "disabled when unauthorised" in new WithApplication {
        val addPostRequest = FakeRequest(POST, "/")
        val addPostResponse = Blog.addPost()(addPostRequest)
        status(addPostResponse) must equalTo(UNAUTHORIZED)
      }
    }
  }
}
