package db.readers

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class View(offset: Long, rows: List[JsObject], totalRows: Long)

object ViewRead {
  implicit val viewReads = (
    (__ \ "offset").read[Long] and
      (__ \ "rows").read[List[JsObject]] and
      (__ \ "total_rows").read[Long]
    )(View)
}
