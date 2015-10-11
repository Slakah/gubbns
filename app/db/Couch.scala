package db

import javax.inject.Inject

import db.ResponseHandler.FutureResponseWithValidate
import play.api.libs.ws.WSResponse

import scala.concurrent.Future

class Couch @Inject() (ws: WebService, config: ConfigService) {
  // TODO: member of ConfigService
  val couchDbUrl = s"${config.config().protocol}://${config.config().host}:${config.config().port}"
  val couchBaseRequest = RequestHolder(ws, couchDbUrl)

  def database(dbName: DatabaseName): Database = new Database(couchBaseRequest.append(dbName.toString))

  def createDatabase(dbName: DatabaseName): Future[WSResponse] = databaseRequest(dbName).put().validate

  private def databaseRequest(dbName: DatabaseName): RequestHolder = couchBaseRequest.append(dbName.toString)
}

