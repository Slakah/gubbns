package db

import db.DatabaseName.StringWithToDatabaseName
import org.specs2.mutable._

class DatabaseNameSpec extends Specification {
  "DatabaseName" should {
    "allow database with name database_test" in {
      val databaseName = "database_test"
      databaseName.asDatabaseName.toString must equalTo(databaseName)
    }

    "throw illegal argument on @ character" in {
      val databaseName = "database_t@est"
      databaseName.asDatabaseName must throwA[IllegalArgumentException]
    }

    "throw illegal argument on space at start" in {
      val databaseName = " database_t@est"
      databaseName.asDatabaseName must throwA[IllegalArgumentException]
    }

    "replace / with %2F2" in {
      val databaseName = "d/atabase_t/est"
      databaseName.asDatabaseName.toString must equalTo("d%2F2atabase_t%2F2est")
    }
  }
}
