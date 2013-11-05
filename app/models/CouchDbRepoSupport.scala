package models

import org.ektorp.CouchDbConnector
import org.ektorp.support.CouchDbRepositorySupport
import scala.collection.JavaConverters._

class  CouchDbRepoSupport[T](clazz: Class[T], db: CouchDbConnector)
  extends CouchDbRepositorySupport[T](clazz, db: CouchDbConnector, clazz.getSimpleName.toLowerCase) {

  def all = {
    super.getAll().asScala.toList
  }

  def view(viewName: String) = {
    db.queryView(createQuery(viewName).includeDocs(true), clazz).asScala.toList
  }

  def view(viewName: String, key: String) = {
    db.queryView(createQuery(viewName).designDocId(stdDesignDocumentId).includeDocs(true).key(key), clazz).asScala.toList
  }
}
