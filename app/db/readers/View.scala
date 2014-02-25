package db.readers

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class ViewRow(id: String, key: JsValue, value: JsValue)

object ViewRowRead {
  implicit val viewRowReads = (
    (__ \ "id").read[String] and
      (__ \ "key").read[JsValue] and
      (__ \ "value").read[JsValue]
    )(ViewRow)
}

case class View(offset: Long, rows: List[ViewRow], totalRows: Long)

object ViewRead {

  implicit val viewReads = (
    (__ \ "offset").read[Long] and
      (__ \ "rows").read[List[ViewRow]] and
      (__ \ "total_rows").read[Long]
    )(View)
}
