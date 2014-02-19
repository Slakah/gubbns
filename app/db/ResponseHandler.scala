package db

import play.api.libs.ws.Response
import play.api.libs.json.JsValue
import db.readers.{CouchError, CouchErrorRead}
import CouchErrorRead._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

case class CouchDbRequestException(reason: String) extends RuntimeException

object ResponseHandler {
  def validate(response: Response): Either[String, Response] = {
    val status = response.status
    if (status < 400)
      Right(response)
    else {
      val errorMessage = errorFromJson(response.json)
        .getOrElse( """CouchDB request failed with status "$response.statusText" code $response.status """)

      Left(errorMessage)
    }
  }

  private def errorFromJson(errorJs: JsValue) = {
    errorJs.asOpt[CouchError].map({
      error =>
        val errorType = error.error
        val reason = error.reason
        s"""CouchDB request failed with error "$errorType" and reason "$reason" """
    })
  }

  implicit class FutureResponseWithValidate(futureResponse: Future[Response]) {
    def validate() = futureResponse.map {
      ResponseHandler.validate
    }

    def validateWithError(): Future[Response] = {
      futureResponse.map {
        response =>
          ResponseHandler.validate(response) match {
            case Right(validResponse) => validResponse
            case Left(errorMsg) => throw new CouchDbRequestException(errorMsg)
          }
      }
    }
  }

}
