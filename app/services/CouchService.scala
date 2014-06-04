package services

import db.{WSWebService, PlayConfigService, Couch}

trait CouchServiceComponent {
  val couchService: CouchService

  trait CouchService {
    val couch: Couch
  }

  class PlayCouchService extends CouchService {
    override val couch: Couch = new Couch() with PlayConfigService with WSWebService
  }
}

