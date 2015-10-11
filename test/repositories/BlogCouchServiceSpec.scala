package repositories

import db.{Couch, DesignDocument, Database}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import db.DatabaseName._


class BlogCouchServiceSpec extends Specification with Mockito {
  "BlogCouchService" should {

    val mockCouch = mock[Couch]

    val blogCouch = new PlayBlogCouchService(mockCouch)

    val mockDatabase = mock[Database]

    "allow access to the blog database" in {
      mockCouch.database("blog".asDatabaseName) returns mockDatabase
      blogCouch.blogDb must beEqualTo(mockDatabase)
    }

    "allow access to the post design document" in {
      val mockDesignDoc = mock[DesignDocument]
      mockDatabase.databaseDesign("post") returns mockDesignDoc
      blogCouch.postDesign must beEqualTo(mockDesignDoc)
    }
  }

}
