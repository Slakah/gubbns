package db.error

import db.models.CouchError
import db.models.CouchErrorRead.couchReads
import play.api.libs.ws.WSResponse

case class CouchException(message: String) extends Exception(message)

object CouchException {
  def apply(response: WSResponse): CouchException = {
    val error = response.json.asOpt[CouchError].getOrElse(CouchError(response.status.toString, response.statusText))
    CouchException(f"""Received an CouchDB error response with error: ${error.error} and reason: ${error.reason}""")
  }
}