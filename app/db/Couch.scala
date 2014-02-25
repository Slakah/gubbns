package db

import scala.concurrent.Future
import db.ResponseHandler.FutureResponseWithValidate
import play.api.libs.ws.Response

class Couch extends ConfigService with WebService {
  val couchDbUrl = s"${config().protocol}://${config().host}:${config().port}"
  val couchBaseRequest = RequestHolder(this, couchDbUrl)

  def database(dbName: DatabaseName): Database = {
    val dbRequestBase = couchBaseRequest.append(dbName.toString)
    new Database(dbRequestBase)
  }

  def createDatabase(dbName: DatabaseName): Future[Response] = {
    val createDbRequest = databaseRequest(dbName).put()
    createDbRequest.validateWithError()
  }

  def deleteDatabase(dbName: DatabaseName): Future[Response] = {
    val deleteDbRequest = databaseRequest(dbName).delete()
    deleteDbRequest.validateWithError()
  }

  private def databaseRequest(dbName: DatabaseName): RequestHolder = {
    couchBaseRequest.append(dbName.toString)
  }
}

