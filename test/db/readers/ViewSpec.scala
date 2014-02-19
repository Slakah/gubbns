package db.readers

import play.api.libs.json._

import org.specs2.mutable.Specification
import db.readers.ViewRead._

class ViewSpec extends Specification {
  "View" should {
    "parse view json" in {
      val viewJson = Json.parse( """{
                           "offset": 0,
                           "rows": [
                               {
                                   "id": "SpaghettiWithMeatballs",
                                   "key": "meatballs",
                                   "value": 1
                               },
                               {
                                   "id": "SpaghettiWithMeatballs",
                                   "key": "spaghetti",
                                   "value": 1
                               },
                               {
                                   "id": "SpaghettiWithMeatballs",
                                   "key": "tomato sauce",
                                   "value": 1
                               }
                           ],
                           "total_rows": 3
                       }""")

      val testView = viewJson.as[View]

      testView.offset should equalTo(0)
      testView.totalRows should equalTo(3)

      testView.rows.size should equalTo(3)

      val row1 = testView.rows(0)
      row1 should equalTo(Json.obj(
        "id" -> "SpaghettiWithMeatballs",
        "key" -> "meatballs",
        "value" -> 1
      ))

      val row2 = testView.rows(1)
      row2 should equalTo(Json.obj(
        "id" -> "SpaghettiWithMeatballs",
        "key" -> "spaghetti",
        "value" -> 1
      ))

      val row3 = testView.rows(2)
      row3 should equalTo(Json.obj(
        "id" -> "SpaghettiWithMeatballs",
        "key" -> "tomato sauce",
        "value" -> 1
      ))

    }
  }
}
