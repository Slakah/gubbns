package db.readers

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.libs.ws.WSResponse

case class CouchError(error: String, reason: String)

object CouchErrorRead {
  implicit val couchReads = (
    (__ \ "error").read[String] and
      (__ \ "reason").read[String]
    )(CouchError)
}