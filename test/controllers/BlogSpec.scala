package controllers

import models.Post
import org.joda.time.{DateTime, DateTimeUtils}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}
import play.filters.csrf.CSRF
import services.PostService

import scala.concurrent.Future

class BlogSpec extends Specification with Mockito with Security {
  "Blog" should {
    DateTimeUtils.setCurrentMillisFixed(DateTime.now.getMillis)

    val title = "Welcome to Gubbns"
    val content = "Gubbns a new website which supports markdown." +
      "Reasons to use:" +
      "* It's written in scala" +
      "* It makes use of Play Framework" +
      "* It uses CouchDB for it's datasore"
    val email = "joe.bloggs@email.com"

    val mockPostsService = mock[PostService]

    val blog = new Blog(mockPostsService)

    "allow adding of posts" in {
      def fakeRequestLoggedOut(title: String = title, content: String = content) = {
        FakeRequest(POST, "/")
          .withFormUrlEncodedBody(
            ("title", title),
            ("content", content))
          .withSession(("csrfToken", CSRF.SignedTokenProvider.generateToken))
      }

      def fakePostRequest(title: String = title, content: String = content) = {
        fakeRequestLoggedOut(title, content)
          .withSession(loginSession(email).data.toSeq:_*)
      }

      "disabled when unauthorised" in new WithApplication {
        val addPostResponse = blog.addPost()(fakeRequestLoggedOut())
        status(addPostResponse) must equalTo(UNAUTHORIZED)
      }

      "add a blog post" in new WithApplication {
        val testPost = Post(title = title, content = content, published = DateTime.now, author = email)

        mockPostsService.add(testPost) returns Future.successful((): Unit)

        val addPostResponse = blog.addPost()(fakePostRequest())
        status(addPostResponse) must equalTo(CREATED)
        there was one(mockPostsService).add(testPost)
      }
    }
  }
}
