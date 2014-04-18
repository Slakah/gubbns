package services

import db.DatabaseName._

trait CouchBlogServiceComponent {
  this: CouchServiceComponent =>

  val couchBlog: CouchBlogService

  class CouchBlogService {
    val db = couchService.couch.database("blog".asDatabaseName)
    val postDesign = db.databaseDesign("post")
  }
}