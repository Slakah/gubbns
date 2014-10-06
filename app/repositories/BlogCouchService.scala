package repositories

import db._
import db.DatabaseName._


trait BlogCouchService {
  def blogDb: Database
  def postDesign: DesignDocument
  def userDesign: DesignDocument
}

trait BlogCouchServiceComponent {
  def blogService: BlogCouchService
}

trait PlayBlogCouchServiceComponent extends BlogCouchServiceComponent {
  this: CouchServiceComponent =>

  override lazy val blogService = PlayBlogCouchService

  object PlayBlogCouchService extends BlogCouchService {
    override lazy val blogDb = couchService.couch.database("blog".asDatabaseName)
    override lazy val postDesign = blogDb.databaseDesign("post")
    override lazy val userDesign = blogDb.databaseDesign("user")
  }
}

trait CouchService {
  def couch: Couch
}

trait CouchServiceComponent {
  def couchService: CouchService
}

trait PlayCouchServiceComponent extends CouchServiceComponent {

  override val couchService = PlayCouchService

  object PlayCouchService extends CouchService {
    override val couch = new Couch() with PlayConfigService with WSWebService
  }
}