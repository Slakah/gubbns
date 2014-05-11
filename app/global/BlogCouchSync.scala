package global

import play.api.{Mode, Application, GlobalSettings}
import play.Logger
import db.{DesignStructure, DatabaseStructure, CouchStructure, CouchSync}
import components.{Default, PlayCouch}
import db.DatabaseName.StringWithToDatabaseName
import db.readers.DesignFormat.designFormats
import db.readers.{ViewFunction, Design}
import play.api.libs.json.Json
import scala.concurrent.{Promise, Future}

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

class BlogCouchSync {
  val couchSync = new CouchSync with PlayCouch

  def sync(): Future[Unit] = {
    Logger.info("Creating couch blog db structure")
    couchSync.sync(BlogStructure.blogStructure)
  }
}

object BlogCouchSync {
  import play.api.Mode

  def apply(app: Application): Future[Unit] = {
    new BlogCouchSync().sync()
  }
}
