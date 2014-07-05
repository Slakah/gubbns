package db

import db.readers.CouchError

import scala.concurrent.Future
import play.api.libs.ws.WSResponse
import db.ResponseHandler.FutureResponseWithValidate

case class Database(dbRequest: RequestHolder) {
  import db.RequestHelper.RequestHelper

  def createIfNoneExist(): Future[Option[CouchError]] = dbRequest.ifNotExist(requestCreate)

  def doesExist: Future[Either[CouchError, Boolean]] = dbRequest.doesExist()

  def create(): Future[Either[CouchError, WSResponse]] = requestCreate().responseWithValidate

  private def requestCreate() = dbRequest.put()

  def getDoc(id: String): Future[Either[CouchError, WSResponse]] = dbRequest.append(id).get().responseWithValidate

  def databaseDesign(designId: String) = DesignDocument(dbRequest.append("_design").append(designId))
}
