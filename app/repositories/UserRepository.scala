package repositories

import db.models.View
import models.User
import play.api.libs.json.{JsString, Json}
import scala.concurrent.Future
import db.models.ViewFormat.viewFormats
import models.UserFormat.userFormats
import play.api.libs.concurrent.Execution.Implicits._


trait UserRepository {
  def fetchByEmail(email: String): Future[Option[User]]
}


trait UserRepositoryComponent {
  def userRepository: UserRepository
}

trait CouchUserRepositoryComponent extends UserRepositoryComponent {
  this: BlogCouchServiceComponent =>

  override val userRepository: UserRepository = CouchUserRepository

  object CouchUserRepository extends UserRepository {
    override def fetchByEmail(email: String): Future[Option[User]] = {
      val emailKey = Json.stringify(JsString(email))
      val byEmailRequest = blogService.postDesign.view("by_email")
      byEmailRequest.map { response =>
        response.json.as[View].rows.map(userRow => userRow.value.as[User]).lift(0)
      }
    }
  }

}
