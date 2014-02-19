package db.readers

import play.api.libs.json._
import play.api.libs.functional.syntax._


case class ViewFunction(name: String, map: Option[String] = None, reduce: Option[String] = None)

object ViewFunctionFormat {
  private val listViewFunctionReads: Reads[List[ViewFunction]] = Reads({
    js =>
      val jsViewFunc = js.as[JsObject].fieldSet.toList.map(jsViewFunc =>
        ViewFunction(
          name = jsViewFunc._1,
          map = (jsViewFunc._2 \ "map").asOpt[String],
          reduce = (jsViewFunc._2 \ "reduce").asOpt[String]
        )
      )
      JsSuccess(jsViewFunc)
  })

  private val listViewFunctionWrites: Writes[List[ViewFunction]] = Writes({
    viewFuncs =>
      def fieldWithJsFunctions(viewFunc: ViewFunction) = (viewFunc.name, Json.obj("map" -> viewFunc.map, "reduce" -> viewFunc.reduce))

      JsObject(viewFuncs.toSeq.map(fieldWithJsFunctions))
  })

  implicit val listViewFunctionFormat: Format[List[ViewFunction]] = Format(listViewFunctionReads, listViewFunctionWrites)

}


case class Design(id: String, rev: Option[String], language: String = "javascript", views: List[ViewFunction])

object DesignFormat {

  import db.readers.ViewFunctionFormat.listViewFunctionFormat


  implicit val designFormats: Format[Design] = (
    (__ \ "_id").format[String] and
      (__ \ "_rev").format[Option[String]] and
      (__ \ "language").format[String] and
      (__ \ "views").format[List[ViewFunction]]
    )(Design, unlift(Design.unapply))
}