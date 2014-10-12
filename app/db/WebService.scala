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

  override def put(url: String, body: String): Future[WSResponse] = WS.url(url).put(body)

  override def put(url: String): Future[WSResponse] = WS.url(url).put("")

  override def get(url: String): Future[WSResponse] = WS.url(url).get()

  override def post(url: String, body: String): Future[WSResponse] = WS.url(url).post(body)

  override def delete(url: String): Future[WSResponse] = WS.url(url).delete()

}
