package services

import models.Post
import org.specs2.mutable.Specification
import repositories.{PostRepository, PostRepositoryComponent}
import org.specs2.mock.Mockito

import scala.concurrent.Future


class PostServiceSpec extends Specification with Mockito
    with PostsComponent with PostRepositoryComponent {
  override val postRepository: PostRepository = mock[PostRepository]

  "PostService" should {
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

    "add a post" in {
      val mockPost = mock[Post]
      val addPostSuccess = Future.successful {}

      postRepository.add(mockPost) returns addPostSuccess
      posts.add(mockPost) must equalTo(addPostSuccess)
    }
  }
}
