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
      val fIfNotExists = for {
        exists <- doesExist()
        if !exists
      } yield {
        f().validate
        ()
      }

      fIfNotExists.fallbackTo(Future(()))
    }

    def doesExist(): Future[Boolean] = request.get().doesDocExist
  }
}
