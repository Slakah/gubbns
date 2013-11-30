package models

import org.ektorp.CouchDbConnector
import play.Logger
import org.joda.time.DateTime

class PostRepository(db: CouchDbConnector)
  extends CouchDbRepoSupport[Post](classOf[Post], db: CouchDbConnector) {

  def byTitle(title: String) = {
    Logger.info(stdDesignDocumentId)

    view("by_title", title)
  }

}
