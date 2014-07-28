package db.models

import play.api.libs.json._

import org.specs2.mutable.Specification
import db.models.ViewRead._

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
                                   "value": 6
                               },
                               {
                                   "id": "SpaghettiWithMeatballs",
                                   "key": "tomato sauce",
                                   "value": 4
                               }
                           ],
                           "total_rows": 3
                       }""")

      val testView = viewJson.as[View]

      testView.offset should equalTo(0)
      testView.totalRows should equalTo(3)

      testView.rows.size should equalTo(3)

      testView.rows(0) should equalTo(
        ViewRow(id = "SpaghettiWithMeatballs",
          key = JsString("meatballs"),
          value = JsNumber(1)))

      testView.rows(1) should equalTo(
        ViewRow(id = "SpaghettiWithMeatballs",
          key = JsString("spaghetti"),
          value = JsNumber(6)))

      testView.rows(2) should equalTo(
        ViewRow(id = "SpaghettiWithMeatballs",
          key = JsString("tomato sauce"),
          value = JsNumber(4)))
    }
  }
}
