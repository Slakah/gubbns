package db

import db.DatabaseName.StringWithToDatabaseName
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mock.Mockito
import org.specs2.mutable._
import play.api.libs.ws.WSResponse

import scala.concurrent.Future

class CouchSpec extends Specification with Mockito {
  "Couch" should {

    val mockWebService = mock[WebService]

    val databaseName = "test_database"

    val couch = new Couch(mockWebService, new MockConfigService)

    "create a database" in { implicit ee: ExecutionEnv =>
      mockWebService.put(s"http://localhost:5984/$databaseName") returns Mocks.validFutureResponse
      val databaseResponse: Future[WSResponse] = couch.createDatabase(databaseName.asDatabaseName)
      databaseResponse must be(Mocks.validResponse).await

      there was one(mockWebService).put(s"http://localhost:5984/$databaseName")
    }
  }
}
