package db

import scala.concurrent.Future
import play.api.libs.ws.Response
import db.ResponseHandler.FutureResponseWithValidate
import play.api.libs.concurrent.Execution.Implicits._
import java.net.URLEncoder
import play.utils.UriEncoding


case class ViewQuery(key: Option[String] = None) {

  def toQueryString: String = {
    if (queryParameters.isEmpty) return ""

    val printParameter = (field: String, value: String) => s"$field=$value"
    val rawQuery = queryParameters.map(printParameter.tupled).mkString("&")
    UriEncoding.encodePathSegment(rawQuery, "utf-8")
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
  import db.RequestHelper.RequestHelper


  def createOrUpdate(json: String) = designRequest.put(json).validateWithError()

  def view(view: String, viewQuery: ViewQuery = ViewQuery()): Future[Response] =
    designRequest.append("_view").append(view).appendQuery(viewQuery.toQueryString).get().validateWithError()

  def doesExist(): Future[Boolean] = designRequest.doesExist()

  def createIfNoneExist(json: String): Future[Unit] = designRequest.ifNotExist(() => createOrUpdate(json))
}
