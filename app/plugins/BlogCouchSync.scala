package plugins

import components.Default
import db.DatabaseName.StringWithToDatabaseName
import db.models.DesignFormat.designFormats
import db.models.{Design, ViewFunction}
import db.{CouchStructure, CouchSync, DatabaseStructure, DesignStructure}
import play.Logger
import play.api.libs.json.Json
import play.api.{Application, Plugin}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.util.{Success, Failure}
import scala.concurrent.duration._

object BlogStructure {

  val postDesign = Design(id = "post",
    views = Set(
      ViewFunction(name = "all", map = Some("function(doc) {if (doc.typeId===\"post\") {emit(doc.published, doc);}}")),
      ViewFunction(name = "by_title", map = Some("function(doc) {if (doc.typeId===\"post\") {emit(decodeURIComponent(doc.title.toLowerCase().replace(/\\s+/g, \"-\")), doc);}}"))
    )
  )

  val userDesign = Design(id = "user",
    views = Set(
      ViewFunction(name = "by_email", map = Some("function(doc) {if (doc.typeId===\"user\") {emit(doc.email, doc);}}"))
    )
  )

  val blogStructure = CouchStructure(
    Set(
      DatabaseStructure(dbName = "blog".asDatabaseName,
        designs = Set(
          DesignStructure("post", Json.prettyPrint(Json.toJson(postDesign))),
          DesignStructure("user", Json.prettyPrint(Json.toJson(userDesign)))
        )
      )
    )
  )

}

object BlogCouchSync extends Default {
  val couchSync = new CouchSync(couchService.couch)

  def sync() = {
    val syncAction = couchSync.sync(BlogStructure.blogStructure)
    syncAction.onComplete {
      case Success(_) => Logger.info("Successfully synced CouchDB design documents")
      case Failure(ex) => Logger.error("While syncing the CouchDB design documents the following error occurred", ex)
    }
    syncAction
  }

}

case class BlogCouchSyncPlugin(app: Application) extends Plugin {
  override def onStart() = {
    Await.ready(BlogCouchSync.sync(), 5.seconds)
  }
}
