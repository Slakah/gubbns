package models

import org.ektorp.support.View
import org.ektorp.{ViewQuery, CouchDbConnector}
import play.Logger

class PostRepository(db: CouchDbConnector)
  extends CouchDbRepoSupport[Post](classOf[Post], db: CouchDbConnector) {

  def byTitle(title: String) = {
    Logger.info(stdDesignDocumentId)

    view("by_title", title)(0)
  }
}
