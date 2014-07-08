package db

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito

class DesignDocumentSpec extends Specification with Mockito {
  "DesignDocument" should {
    "create a design document" in {
      val designJson = "{designJson}"

      val mockRequestHolder = mock[RequestHolder]
      mockRequestHolder.put(designJson) returns Mocks.validFutureResponse

      val designResponse = DesignDocument(mockRequestHolder).create(designJson)

      there was one(mockRequestHolder).put(designJson)

      designResponse must beRight(Mocks.validResponse).await
    }

    "execute a view" in {

      val viewId = "test_view"

      val mockRequestHolder = mock[RequestHolder]
      mockRequestHolder.append("_view") returns mockRequestHolder
      mockRequestHolder.append(viewId) returns mockRequestHolder
      mockRequestHolder.appendQuery("") returns mockRequestHolder

      mockRequestHolder.get() returns Mocks.validFutureResponse

      DesignDocument(mockRequestHolder).view(viewId) must beRight(Mocks.validResponse).await

      there was one(mockRequestHolder).append("_view")
      there was one(mockRequestHolder).append(viewId)
      there was one(mockRequestHolder).appendQuery("")
      there was one(mockRequestHolder).get()
    }
  }
}
