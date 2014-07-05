package db

import db.readers.CouchError
import db.readers.CouchErrorRead._
import play.api.libs.ws.WSResponse

import scala.concurrent.Future

object ResponseHandler {

  def validate(response: WSResponse): Option[CouchError] = {
    if (response.status < 400) {
      None
    } else {
      Some(determineError(response))
    }
  }

  def determineError(response: WSResponse) = response.json.asOpt[CouchError].getOrElse(fallbackError(response))

  private def fallbackError(response: WSResponse) = CouchError(response.status.toString, response.statusText)

  implicit class FutureResponseWithValidate(futureResponse: Future[WSResponse]) {
    def validate: Future[Option[CouchError]] = futureResponse.map(ResponseHandler.validate)

    def responseWithValidate: Future[Either[CouchError, WSResponse]] =
      futureResponse.map(response => ResponseHandler.validate(response).toLeft(response))
  }

}
