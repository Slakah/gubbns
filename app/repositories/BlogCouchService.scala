package repositories

import javax.inject.Inject

import com.google.inject.ImplementedBy
import db._
import db.DatabaseName._

@ImplementedBy(classOf[PlayBlogCouchService])
trait BlogCouchService {
  def blogDb: Database
  def postDesign: DesignDocument
  def userDesign: DesignDocument
}

class PlayBlogCouchService @Inject() (couch: Couch) extends BlogCouchService {
  override lazy val blogDb = couch.database("blog".asDatabaseName)
  override lazy val postDesign = blogDb.databaseDesign("post")
  override lazy val userDesign = blogDb.databaseDesign("user")
}