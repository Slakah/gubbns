package db

import org.specs2.mutable._
import db.DatabaseName.StringWithToDatabaseName
import org.specs2.mock.Mockito

class CouchSpec extends Specification with Mockito {
  "Couch" should {

    val mockWebService = mock[WebService]

    trait MockWebService extends WebService {
      override def put(url: String) = {
        mockWebService.put(url)
      }

      override def delete(url: String) = {
        mockWebService.delete(url)
      }
    }

    val databaseName = "test_database"

    val couch = new Couch with MockWebService with MockConfigService

    "create a database" in {
      mockWebService.put(s"http://localhost:5984/$databaseName") returns Mocks.validFutureResponse

      couch.createDatabase(databaseName.asDatabaseName) should beNone.await

      there was one(mockWebService).put(s"http://localhost:5984/$databaseName")
    }
  }
}
