package db

import scala.concurrent.Future
import play.api.libs.ws.WSResponse
import db.ResponseHandler.FutureResponseWithValidate

case class Database(dbRequest: RequestHolder) {
  import db.RequestHelper.RequestHelper


  def createIfNoneExist(): Future[Unit] = dbRequest.ifNotExist(create)

  def doesExist: Future[Boolean] = dbRequest.doesExist()

  def create(): Future[WSResponse] = {
    val createRequest = dbRequest.put()
    createRequest.validateWithError()
  }

  def createOrUpdateDoc(doc: DocumentBase): Future[WSResponse] = {
    val id = doc.id
    dbRequest.append(id).put(doc.json).validateWithError()
  }

  def getDoc(id: String): Future[WSResponse] = {
    dbRequest.append(id).get().validateWithError()
  }

  def databaseDesign(designId: String) = {
    DesignDocument(dbRequest.append("_design").append(designId))
  }
}
