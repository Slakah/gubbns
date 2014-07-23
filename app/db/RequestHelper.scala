package db

import db.error.CouchException
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws.WSResponse
import db.ResponseHandler.FutureResponseWithValidate
import scala.concurrent.Future
import scala.util.{Failure, Success}

object RequestHelper {
  implicit class RequestHelper(request: RequestHolder) {

    def ifNotExist(f: () => Future[WSResponse]): Future[Unit] = {
      for {
        exists <- doesExist()
        if !exists
      } yield f().validate
    }

    def doesExist(): Future[Boolean] = {
      request.get().map {response =>
        response.status match {
          case 404 => Success(false)
          case 200 => Success(true)
          case errorStatus => Failure(CouchException(response))
        }
      }.flatMap(Future.fromTry(_))
    }
  }
}
