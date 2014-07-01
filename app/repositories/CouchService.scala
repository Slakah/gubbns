package repositories

import db.{Couch, PlayConfigService, WSWebService}

trait CouchService {
  def couch: Couch
}

trait CouchServiceComponent {
  val couchService: CouchService
}

trait PlayCouchServiceComponent extends CouchServiceComponent {
  override val couchService: CouchService = PlayCouchService

  object PlayCouchService extends CouchService {
    override val couch: Couch = new Couch() with PlayConfigService with WSWebService
  }
}