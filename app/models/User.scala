package models

import play.api.libs.json.Json


case class User(email: String,
                hashedPassword: String)


object UserFormat {
  implicit val userReads = Json.reads[User]
}
