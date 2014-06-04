package models

import play.api.libs.json._

import org.specs2.mutable._
import models.PostFormat.postFormats
import org.joda.time.DateTime

class PostSpec extends Specification {
  "Post" should {
    "read post json" in {

      val postJson = Json.parse( """{
                                     "_id": "d8dd32adaac7e6e476dc658458001422",
                                     "_rev": "17-4f9603c31a1813280c46c7e124d62562",
                                     "title": "First",
                                     "author": "Slakah",
                                     "published": "2014-02-12T00:00:00Z",
                                     "content": "Today was a wonderful day in milton keynes",
                                     "type": "post"
                                  }""")
      val post = postJson.as[Post]
      post._id must equalTo("d8dd32adaac7e6e476dc658458001422")
      post._rev must beSome("17-4f9603c31a1813280c46c7e124d62562")
      post.title must equalTo("First")
      post.author must equalTo("Slakah")
      post.published must equalTo(DateTime.parse("2014-02-12"))
      post.content must equalTo("Today was a wonderful day in milton keynes")
      post.typeId must equalTo("post")

    }
  }
}
