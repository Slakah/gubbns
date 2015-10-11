package repositories

import db.{Mocks, Database}
import models.Post
import org.joda.time.DateTime
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.libs.json.{JsString, JsObject, Json}
import models.PostFormat.postFormats

class CouchPostRepositorySpec extends Specification with Mockito {

  val mockBlogService = mock[BlogCouchService]
  val mockBlogDb = mock[Database]
  mockBlogService.blogDb returns mockBlogDb

  val testPostRepo = new CouchPostRepository(mockBlogService)

  "CouchPostRepository" should {
    "add a post" in {
      val testPost = Post(
        title = "Welcome to Gubbns",
        content = "This is blog post content",
        published = DateTime.now,
        author = "Joe Bloggs"
      )
      val postJson = Json.toJson(testPost).as[JsObject]
      val postJsonWithField = postJson + ("typeId" -> JsString("post"))
      val testPostJson = Json.prettyPrint(postJsonWithField)
      mockBlogDb.addDoc(testPostJson) returns Mocks.validFutureResponse
      testPostRepo.add(testPost)
      there was one(mockBlogDb).addDoc(testPostJson)
    }
  }

}
