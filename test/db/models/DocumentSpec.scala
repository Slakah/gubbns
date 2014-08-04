package db.models

import org.specs2.mutable.Specification
import play.api.libs.json.Json
import db.models.DocumentFormat.documentFormats

class DocumentSpec extends Specification {
  case class Foo(foo: String)

  implicit val fooFormat = Json.format[Foo]

  "Document" should {
    "parse document" in {
      val documentJson = Json.parse( """{
                           "_id": "fooid",
                           "_rev": "2-3f80699103e713090fe6e591e4f2edb7",
                           "foo": "bar"
                       }""")

      val testDoc = documentJson.as[Document[Foo]]

      testDoc._id must equalTo("fooid")
      testDoc._rev must beSome("2-3f80699103e713090fe6e591e4f2edb7")
      testDoc.content.foo must equalTo("bar")
    }
  }

}
