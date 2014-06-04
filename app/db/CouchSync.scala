package db

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import scala.util.Success
import services.CouchServiceComponent

case class DesignStructure(name: String, json: String)

case class DatabaseStructure(dbName: DatabaseName, designs: Set[DesignStructure])

case class CouchStructure(databases: Set[DatabaseStructure])

/**
 * Used to sync a proposed couch structure with the couchdb instance
 */
trait CouchSync extends CouchServiceComponent {
  val couch = new Couch() with PlayConfigService with WSWebService

  /**
   * Sync the proposed couch structure with the database
   * @param structure
   * @return
   */
  def sync(structure: CouchStructure): Future[Unit] = {
    val createStructures: Set[Future[Unit]] = structure.databases.map({dbStructure =>
      val database = couch.database(dbStructure.dbName)
      database.createIfNoneExist.andThen({case Success(_) => createDesignsIfNoneExist(database, dbStructure.designs)})
    })
    foldFutures(createStructures)
  }

  private def createDesignsIfNoneExist(database: Database, designs: Set[DesignStructure]): Future[Unit] = {
    val createDesigns: Set[Future[_]] = designs.map({(design: DesignStructure) =>
      database.databaseDesign(design.name).createOrUpdate(design.json)
    })
    foldFutures(createDesigns)
  }

  private def foldFutures(futures: TraversableOnce[Future[_]]): Future[Unit] = {
    val emptyFuture = Future{()}
    futures.foldLeft(emptyFuture)({(f1, f2) => for {
        r1 <- f1
        r2 <- f2
      } yield ()
    })
  }
}
