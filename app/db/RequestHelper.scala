package db

import db.error.CouchException
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws.WSResponse
import db.ResponseHandler.FutureResponseWithValidate
import scala.concurrent.Future
import scala.util.{Try, Failure, Success}

object RequestHelper {
  implicit class RequestHelper(request: RequestHolder) {

    def ifNotExist(f: () => Future[WSResponse]): Future[Unit] = {
      val fIfNotExists = for {
        exists <- doesExist()
        if !exists
      } yield {
        f().validate
        ()
      }

      fIfNotExists.fallbackTo(Future(()))
    }

    def doesExist(): Future[Boolean] = {
      request.get().map {response =>
        response.status match {
          case 404 => Success(false)
          case 200 => Success(true)
          case errorStatus => Failure(CouchException(response))
        }
      }.flatMap(fromTry(_))
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
