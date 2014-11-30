package db

import db.error.CouchException
import play.api.libs.ws.WSResponse

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

object ResponseHandler {

  def validate(response: WSResponse): Try[WSResponse] = {
    if (response.status < 400) Success(response)
    else {
      Failure(CouchException(response))
    }
  }

  def doesDocExist(response: WSResponse): Try[Boolean] = response.status match {
    case 404 => Success(false)
    case 200 => Success(true)
    case errorStatus => Failure(CouchException(response))
  }

  implicit class FutureResponseWithValidate(futureResponse: Future[WSResponse]) {

    def validate: Future[WSResponse] = futureResponse.flatMap {response =>
      Future.fromTry(ResponseHandler.validate(response))
    }

    def doesDocExist: Future[Boolean] = futureResponse.flatMap {response =>
      Future.fromTry(ResponseHandler.doesDocExist(response))
    }
  }

}
