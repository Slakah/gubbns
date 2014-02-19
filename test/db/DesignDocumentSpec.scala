package db

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito

class DesignDocumentSpec extends Specification with Mockito {
  "DesignDocument" should {
    "create a design document" in {
      val mockRequestHolder = mock[RequestHolder]

      val designJson = "{designJson}"
      mockRequestHolder.put(designJson) returns Mocks.validFutureResponse

      DesignDocument(mockRequestHolder).createOrUpdate(designJson)

      there was one(mockRequestHolder).put(designJson)
    }

    "execute a view" in {
      val mockRequestHolder = mock[RequestHolder]


      val designJson = "{designJson}"

      mockRequestHolder.append("_view") returns mockRequestHolder
      mockRequestHolder.append("_view") returns mockRequestHolder

      mockRequestHolder.put(designJson) returns Mocks.validFutureResponse

      DesignDocument(mockRequestHolder).createOrUpdate(designJson)

      there was one(mockRequestHolder).put(designJson)
    }
  }
}
