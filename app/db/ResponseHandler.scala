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

  implicit class FutureResponseWithValidate(futureResponse: Future[WSResponse]) {

    def validate: Future[WSResponse] = futureResponse.flatMap {response =>
      fromTry(ResponseHandler.validate(response))
    }
  }

  /**
   * Replace this with Future.fromTry in scala 2.11
   * @param t
   * @tparam A
   * @return
   */
  def fromTry[A](t: Try[A]): Future[A] = t match {
    case Success(s) => Future.successful(s)
    case Failure(f) => Future.failed(f)
  }
}
