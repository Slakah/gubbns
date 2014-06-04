package db

import scala.concurrent.Future
import db.ResponseHandler.FutureResponseWithValidate
import java.net.URLEncoder
import play.api.libs.ws.WSResponse

/**
 *
 * @param key Should be a json encoded value
 */
case class ViewQuery(key: Option[String] = None) {


  def toQueryString: String = {
    if (queryParameters.isEmpty) return ""

    val printQueryParameter = (field: String, value: String) => {
      val encode = (s: String) => URLEncoder.encode(s, "utf-8")
      s"${encode(field)}=${encode(value)}"
    }

    queryParameters.map(printQueryParameter.tupled).mkString("&")
  }


  private lazy val queryParameters: Map[String, String] = {
    def queryIfExists[A](field: String, opt: Option[A]) = {
      opt match {
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

  def view(view: String, viewQuery: ViewQuery = ViewQuery()): Future[WSResponse] =
    designRequest.append("_view").append(view).appendQuery(viewQuery.toQueryString).get().validateWithError()

  def doesExist(): Future[Boolean] = designRequest.doesExist()

  def createIfNoneExist(json: String): Future[Unit] = designRequest.ifNotExist(() => createOrUpdate(json))
}
