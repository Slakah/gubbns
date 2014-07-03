package repositories

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class CouchUserRepositorySpec extends Specification with Mockito
    with CouchServiceComponent with CouchUserRepositoryComponent {
  override val couchService: CouchService = _

  "CouchServiceComponent" should {
    "find user joe@gmail.com" in {
      userRepository.findByEmail("joe@gmail.com") must beSome("")
    }
  }
}
