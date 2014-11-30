package db

import db.models.Document
import play.api.libs.json.{JsObject, Json}
import scala.concurrent.Future
import db.ResponseHandler.FutureResponseWithValidate
import java.net.URLEncoder
import play.api.libs.ws.WSResponse
import scala.concurrent.ExecutionContext.Implicits.global

import db.models.DocumentFormat.documentFormats

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

  def view(view: String, viewQuery: ViewQuery = ViewQuery()): Future[WSResponse] =
    designRequest.append("_view").append(view).appendQuery(viewQuery.toQueryString).get().validate

  def createIfNoneExist(json: String): Future[Unit] = designRequest.ifNotExist {() => requestCreate(json)}

  def createOrUpdate(json: String): Future[WSResponse] = {
    val designDocResponse = designRequest.get()
    designDocResponse.doesDocExist.flatMap {
      case false => create(json)
      case true => designDocResponse.flatMap { response =>
        val existingDoc = response.json.as[Document[JsObject]]
        val newDoc = Json.parse(json).as[Document[JsObject]]
          .copy(_id = existingDoc._id, _rev = existingDoc._rev)
        create(Json.prettyPrint(Json.toJson(newDoc)))
      }
    }
  }

  def doesExist: Future[Boolean] = designRequest.doesExist()

  def create(json: String): Future[WSResponse] = {
    requestCreate(json).validate
  }

  private val requestCreate: String => Future[WSResponse] = designRequest.put
}
