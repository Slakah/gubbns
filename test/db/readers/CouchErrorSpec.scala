package db.readers

import play.api.libs.json._
import db.readers.CouchErrorRead.couchReads
import org.specs2.mutable._

class CouchErrorSpec extends Specification {
  "CouchError" should {
    "read couch error json" in {

      val errorJson = Json.parse( """{
        "error": "not_found",
        "reason": "deleted"
      }""")
      val couchError = errorJson.as[CouchError]

      val expectedError = CouchError(error = "not_found", reason = "deleted")
      couchError must equalTo(expectedError)
    }
  }
}
