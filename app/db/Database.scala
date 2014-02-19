package db

import scala.concurrent.Future
import play.api.libs.ws.Response
import play.api.libs.concurrent.Execution.Implicits._
import db.ResponseHandler.FutureResponseWithValidate

case class Database(dbRequest: RequestHolder) {

  def createIfNoneExist(): Future[Unit] = doesExist.collect({
    case false => create()
    case true => ()
  })

  def doesExist: Future[Boolean] = {
    val databaseGet = dbRequest.get()
    databaseGet.map({
      response =>
        response.status match {
          case 404 => false
          case 200 => true
          case _ =>
            throw new IllegalStateException( s"""Expected status code OK (200) or Not Found (404), got "${response.status}" """)
        }
    })
  }

  def create(): Future[Response] = {
    val createRequest = dbRequest.put()
    createRequest.validateWithError()
  }

  def createOrUpdateDoc(doc: DocumentBase): Future[Response] = {
    val id = doc.id
    dbRequest.append(id).put(doc.json).validateWithError()
  }

  def getDoc(id: String): Future[Response] = {
    dbRequest.append(id).get().validateWithError()
  }

  def databaseDesign(designId: String) = {
    DesignDocument(dbRequest.append("_design").append(designId))
  }
}
