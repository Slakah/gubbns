package controllers

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}

class BlogSpec extends Specification with Mockito {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dfe924851c67ae256dfaa451b66ef7f13e2fa73a

  "Blog" should {
    "allow adding of posts" in {
      "disabled when unauthorised" in new WithApplication {
<<<<<<< HEAD
=======
  "Blog" should {
    "allow adding of posts" in {
      "redirect when unauthorised" in new WithApplication {
>>>>>>> Added sketch for Blog#addPost, with spec for unauthorised
=======

  "Blog" should {
    "allow adding of posts" in {
      "disabled when unauthorised" in new WithApplication {
>>>>>>> add unauthorised case for add blog post + fix security to allow Future[Result]
=======
>>>>>>> dfe924851c67ae256dfaa451b66ef7f13e2fa73a
        val addPostRequest = FakeRequest(POST, "/")
        val addPostResponse = Blog.addPost()(addPostRequest)
        status(addPostResponse) must equalTo(UNAUTHORIZED)
      }
    }
  }
}
