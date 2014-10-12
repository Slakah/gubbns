package db

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.libs.json.Json


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
      testResponse must be(Mocks.validResponse).await
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

    "create a document" in {
      val testJson = """{"foo": "bar}"""
      val couchResponse = Json.toJson("""{
                            |    "id": "ab39fe0993049b84cfa81acd6ebad09d",
                            |    "ok": true,
                            |    "rev": "1-9c65296036141e575d32ba9c034dd3ee"
                            |}""")

      val mockRequestHolder = mock[RequestHolder]
      val validCouchResponse = Mocks.validFutureResponse(couchResponse)
      mockRequestHolder.post(testJson) returns validCouchResponse

      val testDatabase = Database(mockRequestHolder)
      val addResponse = testDatabase.addDoc(testJson)
      there was one(mockRequestHolder).post(testJson)
      addResponse must be(validCouchResponse)

    }

  }
}
