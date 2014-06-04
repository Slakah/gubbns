package models

import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Post(
                 _id: String,
                 _rev: Option[String] = None,
                 typeId: String,
                 title: String,
                 content: String,
                 published: DateTime,
                 author: String
                 )

object IsoDateTimeFormat extends DefaultFormat with DefaultReads with DefaultWrites {
  val dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"
  val isoJodaDateTimeFormat = GenericFormat[DateTime](jodaDateReads(dateTimePattern),
    jodaDateWrites(dateTimePattern))

}

object PostFormat {
  implicit val isoJodaDateTimeFormat = IsoDateTimeFormat.isoJodaDateTimeFormat

  implicit val postFormats = (
    (__ \ "_id").format[String] and
      (__ \ "_rev").format[Option[String]] and
      (__ \ "type").format[String] and
      (__ \ "title").format[String] and
      (__ \ "content").format[String] and
      (__ \ "published").format[DateTime] and
      (__ \ "author").format[String]
    )(Post, unlift(Post.unapply))

}
