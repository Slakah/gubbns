package repositories

import models.User


trait UserRepository {
  def fetchByEmail(email: String): Option[User]
}


trait UserRepositoryComponent {
  def userRepository: UserRepository
}

trait CouchUserRepositoryComponent extends UserRepositoryComponent {
  this: BlogCouchServiceComponent =>

  override val userRepository: UserRepository = CouchUserRepository

  object CouchUserRepository extends  UserRepository {
    override def fetchByEmail(email: String): Option[User] = ???
  }

}
