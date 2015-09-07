package db

import play.api.Play.current
import play.api.libs.ws.{WS, WSResponse}

import scala.concurrent.Future

trait WebService {
  def put(url: String, body: String): Future[WSResponse]

  def put(url: String): Future[WSResponse]

  def get(url: String): Future[WSResponse]

  def post(url: String, body: String): Future[WSResponse]

  def delete(url: String): Future[WSResponse]
}


class WSWebService extends WebService {
  val writeJsonHeader = ("Content-Type", "application/json")
  val readJsonHeaders = ("Accept", "application/json")

  override def put(url: String, body: String): Future[WSResponse] =
    WS.url(url).withHeaders(writeJsonHeader, readJsonHeaders).put(body)

  override def put(url: String): Future[WSResponse] =
    WS.url(url).withHeaders(writeJsonHeader, readJsonHeaders).put("")

  override def get(url: String): Future[WSResponse] =
    WS.url(url).withHeaders(writeJsonHeader).get()

  override def post(url: String, body: String): Future[WSResponse] =
    WS.url(url).withHeaders(writeJsonHeader, readJsonHeaders).post(body)

  override def delete(url: String): Future[WSResponse] =
    WS.url(url).withHeaders(writeJsonHeader).delete()
}
