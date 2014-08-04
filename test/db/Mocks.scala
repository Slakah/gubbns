package db

import akka.dispatch.Futures
import org.specs2.mock.Mockito
import play.api.libs.json.{JsString, JsValue, Json}
import play.api.libs.ws.WSResponse

import scala.concurrent._

object Mocks extends Mockito {
  val validResponse: WSResponse = validResponse(JsString("test_body"))

  def validResponse(json: JsValue): WSResponse = {
    val mockValidResponse = mock[WSResponse]
    mockValidResponse.status returns 200
    mockValidResponse.body returns Json.prettyPrint(json)
    mockValidResponse.json returns json

    mockValidResponse
  }


  val validFutureResponse: Future[WSResponse] = Futures.successful(validResponse)

  def validFutureResponse(json: JsValue): Future[WSResponse] = Futures.successful(validResponse(json))
}
