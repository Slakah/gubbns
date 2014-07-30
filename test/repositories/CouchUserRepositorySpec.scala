package repositories

import db.DesignDocument
import db.models.{View, ViewRow}
import models.User
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.libs.json.{JsString, Json}
import db.Mocks.validFutureResponse
import models.UserFormat.userFormats
import db.models.ViewFormat.viewFormats


class CouchUserRepositorySpec extends Specification with Mockito {
  val mockBlogService = mock[BlogCouchService]

  object TestCouchUserRepository extends BlogCouchServiceComponent with CouchUserRepositoryComponent {
    override val blogService: BlogCouchService = mockBlogService
  }

  val userRepository = TestCouchUserRepository.userRepository

  "CouchServiceComponent" should {
    "fetch user joe@gmail.com" in {
      val email = "joe@gmail.com"

      val user = User(email, "$2a$10$iXIfki6AefgcUsPqR.niQ.FvIK8vdcfup09YmUxmzS/sQeuI3QOFG")
      val userJson = Json.toJson(user)

      val mockPostDesign = mock[DesignDocument]

      mockBlogService.postDesign returns mockPostDesign

      val view = View(0, List(ViewRow("1", JsString(email), userJson)), 1)

      mockPostDesign.view("by_email") returns validFutureResponse(Json.toJson(view))

      userRepository.fetchByEmail(email) must beSome(user).await
    }
  }
}
