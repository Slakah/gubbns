package db

import db.readers.CouchError

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

  def view(view: String, viewQuery: ViewQuery = ViewQuery()): Future[Either[CouchError, WSResponse]] =
    designRequest.append("_view").append(view).appendQuery(viewQuery.toQueryString).get().responseWithValidate

  def createIfNoneExist(json: String): Future[Option[CouchError]] = designRequest.ifNotExist {()=>requestCreate(json)}

  def doesExist: Future[Either[CouchError, Boolean]] = designRequest.doesExist()

  def create(json: String): Future[Either[CouchError, WSResponse]] = requestCreate(json).responseWithValidate

  private val requestCreate = designRequest.put(_)
}
