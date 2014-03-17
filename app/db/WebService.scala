package db

import play.api.libs.ws.{Response, WS}
import scala.concurrent.Future
import play.api.Logger


trait WebService {
  this: WebService =>

  def put(url: String, body: String): Future[Response] = this.put(url, body)

  def put(url: String): Future[Response] = this.put(url)

  def get(url: String): Future[Response] = this.get(url)


  def delete(url: String): Future[Response] = this.delete(url)
}


trait WSWebService extends WebService {

  override def put(url: String, body: String): Future[Response] = WS.url(url).put(body)

  override def put(url: String): Future[Response] = WS.url(url).put("")

  override def get(url: String): Future[Response] = WS.url(url).get()

  override def delete(url: String): Future[Response] = WS.url(url).delete()

}
