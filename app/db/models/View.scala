package db.models

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class ViewRow(id: String, key: JsValue, value: JsValue)

case class View(offset: Long, rows: List[ViewRow], totalRows: Long)

object ViewFormat {

  implicit val viewRowFormats = Json.format[ViewRow]

  implicit val viewFormats = (
    (__ \ "offset").format[Long] and
      (__ \ "rows").format[List[ViewRow]] and
      (__ \ "total_rows").format[Long]
    )(View.apply, unlift(View.unapply))
}
