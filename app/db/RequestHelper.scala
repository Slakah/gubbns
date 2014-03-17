import db.RequestHolder

package object RequestHelper {
  implicit case class RequestHelper(request: RequestHolder) {
    def doesExist = {
      val requestGet = request.get()
      requestGet.map({
        response =>
          response.status match {
            case 404 => false
            case 200 => true
            case _ =>
              throw new IllegalStateException( s"""Expected status code OK (200) or Not Found (404), got "${response.status}" """)
          }
      })
    }
  }
}
