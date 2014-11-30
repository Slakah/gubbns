package db

import scala.concurrent.Future
import play.api.libs.ws.WSResponse
import db.ResponseHandler.FutureResponseWithValidate
import play.api.libs.concurrent.Execution.Implicits._

case class Database(dbRequest: RequestHolder) {
  import db.RequestHelper.RequestHelper

  def createIfNoneExist(): Future[Unit] = dbRequest.ifNotExist(requestCreate)

  def doesExist: Future[Boolean] = dbRequest.doesExist()

  def create(): Future[WSResponse] = requestCreate().validate

  private def requestCreate() = dbRequest.put()

  def getDoc(id: String): Future[WSResponse] = dbRequest.append(id).get().validate

  def databaseDesign(designId: String) = DesignDocument(dbRequest.append("_design").append(designId))

  def addDoc(json: String): Future[WSResponse] = dbRequest.post(json).validate
}
