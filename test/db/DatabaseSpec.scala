package db

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification


class DatabaseSpec extends Specification with Mockito {
  "Database" should {

    "get a document" in {
      val docId = "doc_id"
      val docBody = "doc_body"

      val mockRequestHolder = mock[RequestHolder]
      mockRequestHolder.append(docId) returns mockRequestHolder
      mockRequestHolder.get() returns Mocks.validFutureResponse

      val testResponse = Database(mockRequestHolder).getDoc(docId)

      there was one(mockRequestHolder).append(docId)
      there was one(mockRequestHolder).get()
      testResponse must beRight(Mocks.validResponse).await
    }

    "create design document accessor" in {
      val designId = "design_id"

      val mockRequestHolder = mock[RequestHolder]
      mockRequestHolder.append("_design") returns mockRequestHolder
      mockRequestHolder.append(designId) returns mockRequestHolder

      val testDesign = Database(mockRequestHolder).databaseDesign(designId)

      there was one(mockRequestHolder).append("_design")
      there was one(mockRequestHolder).append(designId)
      testDesign must equalTo(DesignDocument(mockRequestHolder))
    }

  }
}
