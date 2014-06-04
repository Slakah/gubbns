package db

import scala.concurrent.Future
import play.api.libs.ws.WSResponse


case class RequestHolder(webService: WebService, url: String) {

  def append(toAppend: String) = RequestHolder(webService, s"$url/$toAppend")

  def appendQuery(query: String) = RequestHolder(webService, s"$url?$query")

  def put(body: String): Future[WSResponse] = webService.put(url, body)

  def put(): Future[WSResponse] = webService.put(url)

  def get(): Future[WSResponse] = webService.get(url)

  def delete(): Future[WSResponse] = webService.delete(url)

}
