package db
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

object RequestHelper {
  implicit class RequestHelper(request: RequestHolder) {

    def ifNotExist[A](f: () => Future[A]): Future[Unit] = {
      doesExist().collect({
          case false => f().map(_ => ())
          case true => ()
      })
    }


    def doesExist(): Future[Boolean] = {
      request.get().map(_.status match {
          case 404 => false
          case 200 => true
          case errorStatus =>
            throw new IllegalStateException( s"""Expected status code OK (200) or Not Found (404), got "${errorStatus}" """)
        }
      )
    }
  }
}
