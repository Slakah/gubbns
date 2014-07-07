package plugins

import db.DatabaseName.StringWithToDatabaseName
import db.readers.DesignFormat.designFormats
import db.readers.{CouchError, Design, ViewFunction}
import db.{CouchStructure, CouchSync, DatabaseStructure, DesignStructure}
import play.Logger
import play.api.libs.json.Json
import play.api.{Application, Plugin}
import repositories.PlayCouchServiceComponent

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Success

import scala.concurrent.ExecutionContext.Implicits.global

object BlogStructure {

  val postDesign = Design(id = "post", views = Set(
    ViewFunction(name = "all", map = Some("function(doc) {if (doc.typeId===\"post\") {emit(doc.published, doc);}}")),
    ViewFunction(name = "by_title", map = Some("function(doc) {if (doc.typeId===\"post\") {emit(decodeURIComponent(doc.title.toLowerCase().replace(/\\s+/g, \"-\")), doc);}}"))
  ))

  val blogStructure = CouchStructure(
    Set(
      DatabaseStructure(dbName = "blog".asDatabaseName,
        designs = Set(DesignStructure("post",
          Json.prettyPrint(Json.toJson(postDesign))
        ))
      )
    )
  )

}

object BlogCouchSync {
  val couchSync = new CouchSync with PlayCouchServiceComponent

  def sync() = {
    val sync = couchSync.sync(BlogStructure.blogStructure)
    sync.andThen {
      case Success(possibleErrors) =>
        val errors = possibleErrors.flatten
        if (errors.nonEmpty) logErrors(errors)
    }
  }

  private def logErrors(errors: Set[CouchError]) = {
    Logger.error("An error occurred while syncing design documents: ")
    errors.foreach(error => Logger.error(f" type: ${error.error}, reason: ${error.reason}"))
  }
}

case class BlogCouchSyncPlugin(app: Application) extends Plugin {
  override def onStart() {
    Logger.info("Creating blog couchdb structure")
    Await.ready(BlogCouchSync.sync(), 60.seconds)
  }
}
