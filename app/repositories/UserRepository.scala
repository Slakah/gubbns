package repositories

import javax.inject.Inject

import com.google.inject.ImplementedBy
import db.ViewQuery
import db.models.View
import models.User
import models.UserFormat.userFormats
import db.models.ViewFormat.viewFormats
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsString, Json}

import scala.concurrent.Future

@ImplementedBy(classOf[CouchUserRepository])
trait UserRepository {
  def fetchByEmail(email: String): Future[Option[User]]
}

class CouchUserRepository @Inject() (blogService: BlogCouchService) extends UserRepository {
  override def fetchByEmail(email: String): Future[Option[User]] = {
    val emailKey = Json.stringify(JsString(email))
    val byEmailRequest = blogService.userDesign.view("by_email", ViewQuery(key = Some(emailKey)))
    byEmailRequest.map { response =>
      response.json.as[View].rows.map(userRow => userRow.value.as[User]).lift(0)
    }
  }
}
