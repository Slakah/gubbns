package db.readers

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsObject
import scala.Some


case class ViewFunction(name: String, map: Option[String] = None, reduce: Option[String] = None)

object ViewFunctionFormat {
  private val listViewFunctionReads: Reads[Set[ViewFunction]] = Reads({
    js =>
      val jsonViewFuncs: Set[(String, JsValue)] = js.as[JsObject].fieldSet.toSet
      val viewFuncs: Set[ViewFunction] = jsonViewFuncs.map(jsonViewFunc =>
        ViewFunction(
          name = jsonViewFunc._1,
          map = (jsonViewFunc._2 \ "map").asOpt[String],
          reduce = (jsonViewFunc._2 \ "reduce").asOpt[String]
        )
      )
      JsSuccess(viewFuncs)
  })

  private val listViewFunctionWrites: Writes[Set[ViewFunction]] = Writes({
    viewFuncs =>
      def nameWithJsFunctions(viewFunc: ViewFunction) = {
        import Json.toJson
        def filterNoneViews(view: (String, Option[String])): Option[(String, JsValue)] = view match {
          case (name, Some(viewFunc)) => Some(name, toJson(viewFunc))
          case (name, None) => None
        }

        val validViews: Seq[(String, JsValue)] = Seq(("map", viewFunc.map), ("reduce", viewFunc.reduce)).flatMap(filterNoneViews)
        (viewFunc.name, JsObject(validViews))
      }

      JsObject(viewFuncs.toSeq.map(nameWithJsFunctions))
  })

  implicit val listViewFunctionFormat: Format[Set[ViewFunction]] = Format(listViewFunctionReads, listViewFunctionWrites)

}


case class Design(id: String, rev: Option[String] = None, language: String = "javascript", views: Set[ViewFunction])

object DesignFormat {

  import db.readers.ViewFunctionFormat.listViewFunctionFormat


  implicit val designFormats: Format[Design] = (
    (__ \ "_id").format[String] and
      (__ \ "_rev").formatNullable[String] and
      (__ \ "language").format[String] and
      (__ \ "views").format[Set[ViewFunction]]
    )(Design, unlift(Design.unapply))
}