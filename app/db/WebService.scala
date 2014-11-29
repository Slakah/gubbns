package db

import play.api.libs.ws.{WSResponse, WS}
import scala.concurrent.Future
import play.api.Play.current

trait WebService {
  this: WebService =>

  def put(url: String, body: String): Future[WSResponse] = this.put(url, body)

  def put(url: String): Future[WSResponse] = this.put(url)

  def get(url: String): Future[WSResponse] = this.get(url)

  def post(url: String, body: String): Future[WSResponse] = this.post(url, body)

  def delete(url: String): Future[WSResponse] = this.delete(url)
}


trait WSWebService extends WebService {
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
