package db

import akka.dispatch.Futures
import com.ning.http.client.{Response => AHCResponse}
import org.specs2.mock.Mockito
import play.api.libs.ws.WSResponse

import scala.concurrent._

object Mocks extends Mockito {
  val validResponse: WSResponse = validResponse("test_body")

  private def validResponse(body: String): WSResponse = {
    val mockValidResponse = mock[WSResponse]
    mockValidResponse.status returns 200
    mockValidResponse.body returns body
    mockValidResponse
  }

  val validFutureResponse: Future[WSResponse] = Futures.successful(validResponse)
}
