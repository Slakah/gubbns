package db

import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito

class DesignDocumentSpec extends Specification with Mockito {
  "DesignDocument" should {
    "create a design document" in { implicit ee: ExecutionEnv =>
      val designJson = "{designJson}"

      val mockRequestHolder = mock[RequestHolder]
      mockRequestHolder.put(designJson) returns Mocks.validFutureResponse

      val designResponse = DesignDocument(mockRequestHolder).create(designJson)

      there was one(mockRequestHolder).put(designJson)

      designResponse must be(Mocks.validResponse).await
    }

    "execute a view" in { implicit ee: ExecutionEnv =>

      val viewId = "test_view"

      val mockRequestHolder = mock[RequestHolder]
      mockRequestHolder.append("_view") returns mockRequestHolder
      mockRequestHolder.append(viewId) returns mockRequestHolder
      mockRequestHolder.appendQuery("") returns mockRequestHolder

      mockRequestHolder.get() returns Mocks.validFutureResponse

      DesignDocument(mockRequestHolder).view(viewId) must be(Mocks.validResponse).await

      there was one(mockRequestHolder).append("_view")
      there was one(mockRequestHolder).append(viewId)
      there was one(mockRequestHolder).appendQuery("")
      there was one(mockRequestHolder).get()
    }
  }
}
