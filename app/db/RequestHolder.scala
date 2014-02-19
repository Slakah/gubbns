package db

import scala.concurrent.Future
import play.api.libs.ws.Response


case class RequestHolder(webService: WebService, url: String) {

  def append(toAppend: String) = RequestHolder(webService, appendToUrl(toAppend))

  def put(body: String): Future[Response] = webService.put(url, body)

  def put(): Future[Response] = webService.put(url)

  def get(): Future[Response] = webService.get(url)

  def delete(): Future[Response] = webService.delete(url)


  private def appendToUrl(toAppend: String) = s"$url/$toAppend"
}
