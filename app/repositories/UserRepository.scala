package repositories

import models.User


trait UserRepository {
  def findByEmail(email: String): Option[User]
}


trait UserRepositoryComponent {
  def userRepository: UserRepository
}

trait CouchUserRepositoryComponent extends UserRepositoryComponent {
  this: CouchServiceComponent =>

  override val userRepository: UserRepository = CouchUserRepository

  object CouchUserRepository extends  UserRepository {
    override def findByEmail(email: String): Option[User] = ???

  }

}
