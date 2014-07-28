package db.models

import play.api.libs.json._
import db.models.CouchErrorRead.couchReads
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

    "read bad design doc error" in {
      val errorJson = Json.parse("""{"error":"compilation_error","reason":"OH NOES"}""")
      val couchError = errorJson.as[CouchError]

      val expectedError = CouchError(error = "compilation_error", reason = """OH NOES""")
      couchError must equalTo(expectedError)
    }
  }
}
