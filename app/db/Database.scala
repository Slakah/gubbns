package db

import db.models.CouchError

import scala.concurrent.Future
import play.api.libs.ws.WSResponse
import db.ResponseHandler.FutureResponseWithValidate

case class Database(dbRequest: RequestHolder) {
  import db.RequestHelper.RequestHelper

  def createIfNoneExist(): Future[Unit] = dbRequest.ifNotExist(requestCreate)

  def doesExist: Future[Boolean] = dbRequest.doesExist()

  def create(): Future[WSResponse] = requestCreate().validate

  private def requestCreate() = dbRequest.put()

  def getDoc(id: String): Future[WSResponse] = dbRequest.append(id).get().validate

  def databaseDesign(designId: String) = DesignDocument(dbRequest.append("_design").append(designId))
}
