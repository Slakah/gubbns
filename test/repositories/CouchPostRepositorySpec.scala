package repositories

import db.{Mocks, Database}
import models.Post
import org.joda.time.DateTime
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.libs.json.Json
import models.PostFormat.postFormats

import scala.concurrent.Future

class CouchPostRepositorySpec extends Specification with Mockito {

  val mockBlogService = mock[BlogCouchService]
  val mockBlogDb = mock[Database]
  mockBlogService.blogDb returns mockBlogDb

  object TestCouchPostRepositoryComponent extends CouchPostRepositoryComponent with BlogCouchServiceComponent {
    override def blogService: BlogCouchService = mockBlogService
  }

  "CouchPostRepository" should {
    "add a post" in {
      val testPost = Post(
        title = "Welcome to Gubbns",
        content = "This is blog post content",
        published = DateTime.now,
        author = "Joe Bloggs"
      )
      val testPostJson = Json.prettyPrint(Json.toJson(testPost))
      mockBlogDb.addDoc(testPostJson) returns Mocks.validFutureResponse
      val testPostRepository = TestCouchPostRepositoryComponent.postRepository
      testPostRepository.add(testPost)
      there was one(mockBlogDb).addDoc(testPostJson)
    }
  }

}
