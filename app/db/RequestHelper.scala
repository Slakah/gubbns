package db

import akka.dispatch.Futures
import db.readers.CouchError
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws.WSResponse
import db.ResponseHandler.FutureResponseWithValidate
import scala.concurrent.Future

object RequestHelper {
  implicit class RequestHelper(request: RequestHolder) {

    def ifNotExist(f: () => Future[WSResponse]): Future[Option[CouchError]] = {
      doesExist().flatMap {
        case Right(false) => f().validate
        case Right(true) => Futures.successful(None)
        case Left(error) => Futures.successful(Some(error))
      }
    }

    def doesExist(): Future[Either[CouchError, Boolean]] = {
      request.get().map { response =>
        response.status match {
          case 404 => Right(false)
          case 200 => Right(true)
          case errorStatus => Left(ResponseHandler.determineError(response))
        }
      }
    }
  }
}
