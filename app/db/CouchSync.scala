package db

import akka.dispatch.Futures
import db.readers.CouchError
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import repositories.CouchServiceComponent

import scala.concurrent.Future

case class DesignStructure(name: String, json: String)

case class DatabaseStructure(dbName: DatabaseName, designs: Set[DesignStructure])

case class CouchStructure(databases: Set[DatabaseStructure])

/**
 * Used to sync a proposed couch structure with the couchdb instance
 */
trait CouchSync {
  this: CouchServiceComponent =>

  /**
   * Sync the proposed couch structure with the database
   * @param structure the structure of the databases/designs for a couchdb
   * @return
   */
  def sync(structure: CouchStructure): Future[Set[Option[CouchError]]] = {
    Future.traverse(structure.databases)(createDatabasesAndDesigns).map(_.flatten)
  }

  private def createDatabasesAndDesigns(dbStructure: DatabaseStructure): Future[Set[Option[CouchError]]] = {
    val database = couchService.couch.database(dbStructure.dbName)
    database.createIfNoneExist().flatMap {
      case error: Some => Futures.successful(Set(error))
      case _ => createDesigns(database, dbStructure.designs)
    }
  }

  private def createDesigns(database: Database, designs: Set[DesignStructure]): Future[Set[Option[CouchError]]] = {
    Future.traverse(designs){design => database.databaseDesign(design.name).createIfNoneExist(design.json)}
  }
}
