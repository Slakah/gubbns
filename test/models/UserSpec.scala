package models

import org.specs2.mutable.Specification
import play.api.libs.json.Json

import models.UserFormat.userReads

class UserSpec extends Specification {
  "User" should {
    "read user json" in {

      val userJson = Json.parse( """{
                                   "_id": "d8dd32adaac7e6e476dc658458001422",
                                   "_rev": "17-4f9603c31a1813280c46c7e124d62562",
                                   "email": "joe@gmail.com",
                                   "hashedPassword": "$2a$10$iXIfki6AefgcUsPqR.niQ.FvIK8vdcfup09YmUxmzS/sQeuI3QOFG"
                                }""")
      val user = userJson.as[User]
      user.email must equalTo("joe@gmail.com")
      user.hashedPassword must equalTo("$2a$10$iXIfki6AefgcUsPqR.niQ.FvIK8vdcfup09YmUxmzS/sQeuI3QOFG")
    }
  }
}
