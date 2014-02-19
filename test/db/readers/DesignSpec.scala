package db.readers

import org.specs2.mutable.Specification
import play.api.libs.json.Json


class DesignSpec extends Specification {
  "Design" should {
    import db.readers.DesignFormat.designFormats

    "parse design json" in {
      val designJson = Json.parse( """{
                                     "_id":"_design/company",
                                     "_rev":"12345",
                                     "language": "javascript",
                                     "views":
                                     {
                                       "all": {
                                         "map": "function(doc) { if (doc.Type == 'customer')  emit(null, doc) }"
                                       },
                                       "by_lastname": {
                                         "map": "function(doc) { if (doc.Type == 'customer')  emit(doc.LastName, doc) }"
                                       },
                                       "total_purchases": {
                                         "map": "function(doc) { if (doc.Type == 'purchase')  emit(doc.Customer, doc.Amount) }",
                                         "reduce": "function(keys, values) { return sum(values) }"
                                       }
                                     }
                                   }""")

      val testDesign = designJson.as[Design]

      testDesign.id must equalTo("_design/company")
      testDesign.rev should equalTo(Some("12345"))
      testDesign.language must equalTo("javascript")
      testDesign.views.size should equalTo(3)

      testDesign.views should contain(ViewFunction("all",
        Some("function(doc) { if (doc.Type == 'customer')  emit(null, doc) }")))

      testDesign.views should contain(ViewFunction("by_lastname",
        Some("function(doc) { if (doc.Type == 'customer')  emit(doc.LastName, doc) }")))

      testDesign.views should contain(ViewFunction("total_purchases",
        Some("function(doc) { if (doc.Type == 'purchase')  emit(doc.Customer, doc.Amount) }"),
        Some("function(keys, values) { return sum(values) }")))


    }
  }

  "ViewFunction" should {
    import db.readers.ViewFunctionFormat.listViewFunctionFormat

    "parse view function map only json" in {
      val designJson = Json.parse( """{
                                    "all": {
                                      "map": "function(doc) { if (doc.Type == 'customer')  emit(null, doc) }"
                                    }
                                  }""")

      val viewFunctions = designJson.as[List[ViewFunction]]
      val viewFunction = viewFunctions(0)

      viewFunction.name must equalTo("all")
      viewFunction.reduce must equalTo(None)
      viewFunction.map must equalTo(Some("function(doc) { if (doc.Type == 'customer')  emit(null, doc) }"))
    }

    "parse view function map and reduce json" in {
      val designJson = Json.parse( """{
                                    "total_purchases": {
                                      "map": "function(doc) { if (doc.Type == 'purchase')  emit(doc.Customer, doc.Amount) }",
                                      "reduce": "function(keys, values) { return sum(values) }"
                                    }
                                  }""")

      val viewFunctions = designJson.as[List[ViewFunction]]
      val viewFunction = viewFunctions(0)

      viewFunction.name must equalTo("total_purchases")
      viewFunction.map must equalTo(Some("function(doc) { if (doc.Type == 'purchase')  emit(doc.Customer, doc.Amount) }"))
      viewFunction.reduce must equalTo(Some("function(keys, values) { return sum(values) }"))

    }
  }

}
