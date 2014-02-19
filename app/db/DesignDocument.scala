package db

import scala.concurrent.Future
import play.api.libs.ws.Response
import db.ResponseHandler.FutureResponseWithValidate

case class DesignDocument(designRequest: RequestHolder) {

  def createOrUpdate(json: String) = designRequest.put(json).validateWithError()

  def executeView(view: String): Future[Response] =
    designRequest.append("_view").append(view).get().validateWithError()


}
