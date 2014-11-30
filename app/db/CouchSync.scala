package db

import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

case class DesignStructure(name: String, json: String)

case class DatabaseStructure(dbName: DatabaseName, designs: Set[DesignStructure])

case class CouchStructure(databases: Set[DatabaseStructure])

/**
 * Used to sync a proposed couch structure with the couchdb instance
 */
class CouchSync(couch: Couch) {

  /**
   * Sync the proposed couch structure with the database
   * @param structure the structure of the databases/designs for a couchdb
   * @return
   */
  def sync(structure: CouchStructure): Future[Set[Unit]] = {
    Future.traverse(structure.databases)(createDatabasesAndDesigns).map(_.flatten)
  }

  private def createDatabasesAndDesigns(dbStructure: DatabaseStructure): Future[Set[Unit]] = {
    val database = couch.database(dbStructure.dbName)
    database.createIfNoneExist().flatMap { _ =>
      createDesigns(database, dbStructure.designs)
    }
  }

  private def createDesigns(database: Database, designs: Set[DesignStructure]): Future[Set[Unit]] = {
    Future.traverse(designs){design => database.databaseDesign(design.name).createOrUpdate(design.json).map{ _ =>
      ()
    }}
  }
}
