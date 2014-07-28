package plugins

import db.DatabaseName.StringWithToDatabaseName
import db.models.DesignFormat.designFormats
import db.models.{Design, ViewFunction}
import db.{CouchStructure, CouchSync, DatabaseStructure, DesignStructure}
import play.Logger
import play.api.libs.json.Json
import play.api.{Application, Plugin}
import repositories.PlayCouchServiceComponent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

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
    couchSync.sync(BlogStructure.blogStructure).onComplete {
      case Success(_) => Logger.info("Successfully synced CouchDB design documents")
      case Failure(ex) => Logger.error("While syncing the CouchDB design documents the following error occurred", ex)
    }
  }

}

case class BlogCouchSyncPlugin(app: Application) extends Plugin {
  override def onStart() {
    BlogCouchSync.sync()
  }
}
