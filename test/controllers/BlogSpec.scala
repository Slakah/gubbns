package controllers

import models.Post
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}
import play.filters.csrf.CSRF
import services.{PegdownServiceComponent, PostService}

import scala.concurrent.Future

class BlogSpec extends Specification with Mockito with Security {
  "Blog" should {

    val title = "Welcome to Gubbns"
    val content = "Gubbns a new website which supports markdown." +
      "Reasons to use:" +
      "* It's written in scala" +
      "* It makes use of Play Framework" +
      "* It uses CouchDB for it's datasore"

    val mockPostsService = mock[PostService]
    mockPostsService.add(any[Post]) returns Future {}

    object TestBlog extends BlogImpl with PegdownServiceComponent {
      override val posts: PostService = mockPostsService
    }

    "allow adding of posts" in {
      def fakeRequestLoggedOut(title: String = title, content: String = content) = {
        FakeRequest(POST, "/")
          .withFormUrlEncodedBody(
            ("title", title),
            ("content", content))
          .withSession((CSRF.TokenName, CSRF.SignedTokenProvider.generateToken))
      }

      def fakePostRequest(title: String = title, content: String = content) = {
        fakeRequestLoggedOut(title, content)
          .withSession(loginSession("joe.bloggs@email.com").data.toSeq:_*)
      }

      "disabled when unauthorised" in new WithApplication {
        val addPostResponse = Blog.addPost()(fakeRequestLoggedOut())
        status(addPostResponse) must equalTo(UNAUTHORIZED)
      }

      "add a blog post" in new WithApplication {
        val addPostResponse = TestBlog.addPost()(fakePostRequest())
        status(addPostResponse) must equalTo(CREATED)
        there was one(mockPostsService).add(any[Post])
      }
    }
  }
}
