package db

import scala.concurrent.Future
import play.api.libs.ws.Response
import db.ResponseHandler.FutureResponseWithValidate
import play.api.libs.concurrent.Execution.Implicits._


case class DesignDocument(designRequest: RequestHolder) {

  def createOrUpdate(json: String) = designRequest.put(json).validateWithError()

  def view(view: String): Future[Response] =
    designRequest.append("_view").append(view).get().validateWithError()

  def doesExist(): Future[Boolean] = {
    designRequest.get().map({
      response =>
        response.status match {
          case 404 => false
          case 200 => true
          case _ =>
            throw new IllegalStateException( s"""Expected status code OK (200) or Not Found (404), got "${response.status}" """)
        }
    })
  }

  def createIfNoneExist(json: String): Future[Unit] = doesExist.collect({
    case false => createOrUpdate(json: String)
    case true => ()
  })
}
