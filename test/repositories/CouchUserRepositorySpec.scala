package repositories

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class CouchUserRepositorySpec extends Specification with Mockito {

  object TestCouchUserRepository extends CouchServiceComponent with CouchUserRepositoryComponent {
    override def couchService: CouchService = mock[CouchService]
  }

  "CouchServiceComponent" should {
    "find user joe@gmail.com" in {
      userRepository.fetchByEmail("joe@gmail.com") must beSome("")
    }
  }
}
