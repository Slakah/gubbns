package db.models

import org.specs2.mutable.Specification
import play.api.libs.json.{JsString, Json}
import db.models.DocumentFormat.documentFormats

class DocumentSpec extends Specification {
  case class Foo(foo: String)

  implicit val fooFormat = Json.format[Foo]

  "Document" should {
    "parse document" in {
      val documentJson = Json.parse( """{
                           "_id": "foo/id",
                           "_rev": "2-3f80699103e713090fe6e591e4f2edb7",
                           "foo": "bar"
                       }""")

      val testDoc = documentJson.as[Document[Foo]]

      testDoc._id must equalTo("foo/id")
      testDoc._rev must beSome("2-3f80699103e713090fe6e591e4f2edb7")
      testDoc.content.foo must equalTo("bar")
    }

    "write a document" in {
      val document = Document(
        _id = "foo/id", _rev = Some("2-3f80699103e713090fe6e591e4f2edb7"),
        content = Foo("bar")
      )
      val documentJson = Json.toJson(document)
      (documentJson \ "_id").get must equalTo(JsString("foo/id"))
      (documentJson \ "_rev").get must equalTo(JsString("2-3f80699103e713090fe6e591e4f2edb7"))
      (documentJson \ "foo").get must equalTo(JsString("bar"))
    }
  }

}
