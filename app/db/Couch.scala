package db

import db.ResponseHandler.FutureResponseWithValidate
import db.readers.CouchError

import scala.concurrent.Future

class Couch extends ConfigService with WebService {
  val couchDbUrl = s"${config().protocol}://${config().host}:${config().port}"
  val couchBaseRequest = RequestHolder(this, couchDbUrl)

  def database(dbName: DatabaseName): Database = new Database(couchBaseRequest.append(dbName.toString))

  def createDatabase(dbName: DatabaseName): Future[Option[CouchError]] = databaseRequest(dbName).put().validate

  private def databaseRequest(dbName: DatabaseName): RequestHolder = couchBaseRequest.append(dbName.toString)
}

