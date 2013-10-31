package models

import org.ektorp.CouchDbConnector
import org.ektorp.support.CouchDbRepositorySupport

class  CouchDbRepoSupport[T](clazz: Class[T], db: CouchDbConnector)
  extends CouchDbRepositorySupport[T](clazz, db: CouchDbConnector) {

}
