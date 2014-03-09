package global

import play.api.{Application, GlobalSettings}
import play.Logger
import db.{DesignStructure, DatabaseStructure, CouchStructure, CouchSync}
import components.{Default, PlayCouch}
import db.DatabaseName.StringWithToDatabaseName
import db.readers.DesignFormat.designFormats
import db.readers.{ViewFunction, Design}
import play.api.libs.json.Json

object BlogStructure {
  val blogStructure = CouchStructure(
    List(
      DatabaseStructure(dbName = "blog".asDatabaseName,
        designs = List(DesignStructure("post",
          Json.prettyPrint(Json.toJson(Design(id = "post", views = List(ViewFunction(name = "all", map = Some("function(doc) {if (doc.type=\"post\") {emit(doc.published, doc);}}"))))))
        ))
      )
    )
  )
}

class BlogCouchSync extends GlobalSettings {
  val couchSync = new CouchSync with Default

  override def onStart(app: Application) {
    Logger.info("Creating couch blog db structure")
    couchSync.sync(BlogStructure.blogStructure)
  }
}

object BlogCouchSync {
  def apply() = new BlogCouchSync
}
