package db

import scala.concurrent.Future
import play.api.libs.ws.Response
import db.ResponseHandler.FutureResponseWithValidate
import play.api.libs.concurrent.Execution.Implicits._
import java.net.URLEncoder


case class ViewQuery(key: Option[String] = None) {

  def toQueryString: String = {
    if (queryParameters.isEmpty) return ""

    val printParameter = (field: String, value: String) => field+"="+value
    val rawQuery = queryParameters.map(printParameter.tupled).mkString("&")
    URLEncoder.encode(rawQuery, "UTF-8")
  }

  private lazy val queryParameters: Map[String, String] = {
    def queryIfExists[A](field: String, value: Option[A]) = {
      value match {
        case Some(value) => Seq((field, value))
        case None => Nil
      }
    }

    Map() ++ queryIfExists("key", key)
  }

}

case class DesignDocument(designRequest: RequestHolder) {

  def createOrUpdate(json: String) = designRequest.put(json).validateWithError()

  def view(view: String, viewQuery: ViewQuery = ViewQuery()): Future[Response] =
    designRequest.append("_view").append(view).appendQuery(viewQuery.toQueryString).get().validateWithError()

  def doesExist(): Future[Boolean] = {
    designRequest.get().map({
      response =>
        response.status match {
          case 404 => false
          case 200 => true
          case _ =>
            throw new IllegalStateException( s"""Expected status code OK (200) or Not Found (404), got "${response.status}" """)
        }
    })
  }

  def createIfNoneExist(json: String): Future[Unit] = doesExist.collect({
    case false => createOrUpdate(json: String)
    case true => ()
  })
}
