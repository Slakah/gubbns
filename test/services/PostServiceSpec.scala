package services

import models.{DisplayPost, Post}
import org.joda.time.DateTime
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification
import play.twirl.api.Html
import repositories.PostRepository
import org.specs2.mock.Mockito
import util.MarkdownProcessor

import scala.concurrent.Future


class PostServiceSpec extends Specification with Mockito {
  val mockPostRepository = mock[PostRepository]
  val mockMarkdown = mock[MarkdownProcessor]

  val testPosts = new Posts(mockMarkdown, mockPostRepository)
  val testPost = Post("Title", "content", DateTime.now, "James")


  "PostService" should {
    "find post with title 'First!' and process the markdown content" in { implicit ee: ExecutionEnv =>

      val response = Future { Some(testPost) }

      val mockMarkdownContent = mock[Html]
      mockMarkdown.apply(testPost.content) returns mockMarkdownContent
      val expectedDisplayPost = DisplayPost(testPost)(mockMarkdown)

      mockPostRepository.findByTitle("First!") returns response
      testPosts.findByTitleDisplay("First!") must beSome(expectedDisplayPost).await
    }

    "find post with title 'First!'" in {
      val mockPost = mock[Future[Option[Post]]]

      mockPostRepository.findByTitle("First!") returns mockPost
      testPosts.findByTitle("First!") must beEqualTo(mockPost)
    }

    "get all posts" in {
      val mockPosts = mock[Future[List[Post]]]

      mockPostRepository.getAll returns mockPosts
      testPosts.getAll must equalTo(mockPosts)
    }

    "get all posts and process the markdown content" in { implicit ee: ExecutionEnv =>
      val mockPosts = Future {
        List(testPost)
      }

      val mockMarkdownContent = mock[Html]
      mockMarkdown.apply(testPost.content) returns mockMarkdownContent
      val expectedDisplayPost = DisplayPost(testPost)(mockMarkdown)

      mockPostRepository.getAll returns mockPosts
      testPosts.getAllDisplay must contain(expectedDisplayPost).await
    }

    "add a post" in {
      val mockPost = mock[Post]
      val addPostSuccess = Future.successful {}

      mockPostRepository.add(mockPost) returns addPostSuccess
      testPosts.add(mockPost) must equalTo(addPostSuccess)
    }
  }
}
