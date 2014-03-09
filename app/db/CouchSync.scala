package db

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import scala.util.Success
import services.CouchServiceComponent
import play.Logger

case class DesignStructure(name: String, json: String)

case class DatabaseStructure(dbName: DatabaseName, designs: List[DesignStructure])

case class CouchStructure(databases: List[DatabaseStructure])

trait CouchSync extends CouchServiceComponent {
  val couch = new Couch() with PlayConfigService with WSWebService

  /**
   * Sync the proposed couch structure with the database
   * @param structure
   * @return
   */
  def sync(structure: CouchStructure): Future[Unit] = {
    val createStructures: List[Future[Unit]] = structure.databases.map({dbStructure =>
      val database = couch.database(dbStructure.dbName)
      database.createIfNoneExist.andThen({case Success(_) => createDesignsIfNoneExist(database, dbStructure.designs)})
    })
    foldFutures(createStructures)
  }

  private def createDesignsIfNoneExist(database: Database, designs: List[DesignStructure]): Future[Unit] = {
    val createDesigns: List[Future[Unit]] = designs.map({(design: DesignStructure) =>
      database.databaseDesign(design.name).createIfNoneExist(design.json)
    })
    foldFutures(createDesigns)
  }

  private def foldFutures(futures: Seq[Future[Unit]]): Future[Unit] = {
    futures.foldLeft(Future[Unit]())({(f1, f2) => for {
        r1 <- f1
        r2 <- f2
      } yield ()
    })
  }
}
