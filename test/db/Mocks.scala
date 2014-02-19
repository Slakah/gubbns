package db

import org.specs2.mock.Mockito
import com.ning.http.client.{Response => AHCResponse}
import scala.concurrent._
import ExecutionContext.Implicits.global
import play.api.libs.ws.Response

object Mocks extends Mockito {
  def validResponse(body: String): Response = {
    val mockValidResponse = mock[AHCResponse]
    mockValidResponse.getStatusCode returns 200
    mockValidResponse.getResponseBody returns body
    Response(mockValidResponse)
  }

  def validFutureResponse: Future[Response] = {
    future {
      val mockValidResponse = mock[AHCResponse]
      mockValidResponse.getStatusCode returns 200
      Response(mockValidResponse)
    }
  }

  def validFutureResponse(body: String): Future[Response] = {
    future {
      validResponse(body)
    }
  }
}
