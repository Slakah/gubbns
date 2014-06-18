package plugins

import play.api.{Application, Plugin}
import play.Logger
import db.{DesignStructure, DatabaseStructure, CouchStructure, CouchSync}
import components.PlayCouch
import db.DatabaseName.StringWithToDatabaseName
import db.readers.DesignFormat.designFormats
import db.readers.{ViewFunction, Design}
import play.api.libs.json.Json
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object BlogStructure {

  val postDesign = Design(id = "post", views = Set(
    ViewFunction(name = "all", map = Some("function(doc) {if (doc.type===\"post\") {emit(doc.published, doc);}}")),
    ViewFunction(name = "by_title", map = Some("function(doc) {if (doc.type===\"post\") {emit(decodeURIComponent(doc.title.toLowerCase().replace(/\\s+/g, \"-\")), doc);}}"))
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
  val couchSync = new CouchSync with PlayCouch

  def sync(): Future[Unit] = {
    couchSync.sync(BlogStructure.blogStructure)
  }
}

case class BlogCouchSyncPlugin(app: Application) extends Plugin {
  override def onStart() {
    Logger.info("Creating blog couchdb structure")
    Await.ready(BlogCouchSync.sync(), 60.seconds)
  }
}
