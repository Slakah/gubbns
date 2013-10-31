package models

import org.ektorp.support.View
import org.ektorp.CouchDbConnector

class PostRepository(db: CouchDbConnector) extends CouchDbRepoSupport[Post](classOf[Post], db: CouchDbConnector) {

}
