package db

import org.specs2.mock.Mockito
import com.ning.http.client.{Response => AHCResponse}
import scala.concurrent._
import ExecutionContext.Implicits.global
import play.api.libs.ws.WSResponse
import play.api.libs.ws.ning.NingWSResponse

object Mocks extends Mockito {
  def validResponse(body: String): WSResponse = {
    val mockValidResponse = mock[AHCResponse]
    mockValidResponse.getStatusCode returns 200
    mockValidResponse.getResponseBody returns body
    NingWSResponse(mockValidResponse)
  }

  def validFutureResponse: Future[WSResponse] = {
    Future {
      val mockValidResponse = mock[AHCResponse]
      mockValidResponse.getStatusCode returns 200
      NingWSResponse(mockValidResponse)
    }
  }

  def validFutureResponse(body: String): Future[WSResponse] = {
    Future {
      validResponse(body)
    }
  }
}
