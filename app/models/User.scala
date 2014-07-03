package models

import play.api.libs.json.Json


case class User(_id: String,
                _rev: Option[String] = None,
                email: String,
                hashedPassword: String)


object UserFormat {
  implicit val userReads = Json.reads[User]
}
