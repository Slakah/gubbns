package services

import models.{DisplayPost, Post}
import org.joda.time.DateTime
import org.specs2.mutable.Specification
import play.twirl.api.Html
import repositories.{PostRepository, PostRepositoryComponent}
import org.specs2.mock.Mockito

import scala.concurrent.Future


class PostServiceSpec extends Specification with Mockito
    with PostsComponent with PostRepositoryComponent with MarkdownServiceComponent {
  val mockPostRepository = mock[PostRepository]
  override val postRepository: PostRepository = mockPostRepository
  val mockMarkdown = mock[MarkdownService]
  override val markdown = mockMarkdown

  object TestPostServiceComponent extends PostsComponent
      with PostRepositoryComponent with MarkdownServiceComponent {
    override val postRepository: PostRepository = mockPostRepository
    override val markdown = mockMarkdown
  }


  val postService = TestPostServiceComponent.posts
  val testPost = Post("Title", "content", DateTime.now, "James")


  "PostService" should {
    "find post with title 'First!' and process the markdown content" in {

      val response = Future { Some(testPost) }

      val mockMarkdownContent = mock[Html]
      mockMarkdown.apply(testPost.content) returns mockMarkdownContent
      val expectedDisplayPost = DisplayPost(testPost)(mockMarkdown)

      postRepository.findByTitle("First!") returns response
      postService.findByTitleDisplay("First!") must beSome(expectedDisplayPost).await
    }

    "find post with title 'First!'" in {
      val mockPost = mock[Future[Option[Post]]]

      postRepository.findByTitle("First!") returns mockPost
      posts.findByTitle("First!") must beEqualTo(mockPost)
    }

    "get all posts" in {
      val mockPosts = mock[Future[List[Post]]]

      postRepository.getAll returns mockPosts
      posts.getAll must equalTo(mockPosts)
    }

    "get all posts and process the markdown content" in {
      val mockPosts = Future {
        List(testPost)
      }

      val mockMarkdownContent = mock[Html]
      mockMarkdown.apply(testPost.content) returns mockMarkdownContent
      val expectedDisplayPost = DisplayPost(testPost)(mockMarkdown)

      postRepository.getAll returns mockPosts
      posts.getAllDisplay must contain(expectedDisplayPost).await
    }

    "add a post" in {
      val mockPost = mock[Post]
      val addPostSuccess = Future.successful {}

      postRepository.add(mockPost) returns addPostSuccess
      posts.add(mockPost) must equalTo(addPostSuccess)
    }
  }
}
