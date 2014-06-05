package db

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import scala.concurrent._
import ExecutionContext.Implicits.global


class DatabaseSpec extends Specification with Mockito {
  "Database" should {

    val docId = "doc_id"
    val docBody = "doc_body"

    "create or update a document" in {
      val mockRequestHolder = mock[RequestHolder]

      mockRequestHolder.append(docId) returns mockRequestHolder
      mockRequestHolder.put(docBody) returns Mocks.validFutureResponse

      Database(mockRequestHolder).createOrUpdateDoc(DocumentBase(docId, docBody))

      there was one(mockRequestHolder).append(docId)
      there was one(mockRequestHolder).put(docBody)
    }

    "get a document" in {
      val mockRequestHolder = mock[RequestHolder]

      mockRequestHolder.append(docId) returns mockRequestHolder
      val expectedResponse = Mocks.validResponse(docBody)
      mockRequestHolder.get() returns Future {
        expectedResponse
      }

      val testResponse = Database(mockRequestHolder).getDoc(docId)

      there was one(mockRequestHolder).append(docId)
      there was one(mockRequestHolder).get()
      testResponse must equalTo(expectedResponse).await
    }

    "create design document accessor" in {
      val mockRequestHolder = mock[RequestHolder]

      val designId = "design_id"

      mockRequestHolder.append("_design") returns mockRequestHolder
      mockRequestHolder.append(designId) returns mockRequestHolder

      val expectedResponse = Mocks.validResponse(docBody)
      mockRequestHolder.get() returns Future {
        expectedResponse
      }

      val testDesign = Database(mockRequestHolder).databaseDesign(designId)

      there was one(mockRequestHolder).append("_design")
      there was one(mockRequestHolder).append(designId)
      testDesign must equalTo(DesignDocument(mockRequestHolder))
    }

  }
}
