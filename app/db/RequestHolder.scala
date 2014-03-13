package db

import scala.concurrent.Future
import play.api.libs.ws.Response
import play.Logger


case class RequestHolder(webService: WebService, url: String) {

  def append(toAppend: String) = RequestHolder(webService, s"$url/$toAppend")

  def appendQuery(query: String) = RequestHolder(webService, s"$url?$query")

  def put(body: String): Future[Response] = webService.put(url, body)

  def put(): Future[Response] = webService.put(url)

  def get(): Future[Response] = { Logger.info(url)
    webService.get(url)}

  def delete(): Future[Response] = webService.delete(url)

}
