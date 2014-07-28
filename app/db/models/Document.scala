package db.models

import scala.reflect.runtime.universe.TypeTag

import play.api.libs.functional.syntax._
import play.api.libs.json._



case class Document[A](_id: String, _rev: Option[String], content: A)

object DocumentFormat {

  implicit def documentFormats[A: TypeTag](implicit fjs: Format[A]): Format[Document[A]] = (
    (__ \ "_id").format[String] and
      (__ \ "_rev").formatNullable[String] and
      __.format[A]
    )(Document.apply, unlift(Document.unapply))

}