package repositories

import db.{Couch, DesignDocument, Database}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import db.DatabaseName._


class BlogCouchServiceSpec extends Specification with Mockito {
  "BlogCouchService" should {

    object TestCouchServiceComponent extends CouchServiceComponent with PlayBlogCouchServiceComponent {
      override val couchService: CouchService = mock[CouchService]
    }

    val blogCouchService = TestCouchServiceComponent

    val mockCouch = mock[Couch]
    blogCouchService.couchService.couch returns mockCouch

    val mockDatabase = mock[Database]

    "allow access to the blog database" in {
      mockCouch.database("blog".asDatabaseName) returns mockDatabase
      blogCouchService.blogService.blogDb must beEqualTo(mockDatabase)
    }

    "allow access to the post design document" in {
      val mockDesignDoc = mock[DesignDocument]
      mockDatabase.databaseDesign("post") returns mockDesignDoc
      blogCouchService.blogService.postDesign must beEqualTo(mockDesignDoc)
    }
  }

}
