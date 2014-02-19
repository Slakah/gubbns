package db

case class DatabaseName private(dbName: String) {
  override def toString = dbName
}

object DatabaseName {
  def valueOf(dbName: String): DatabaseName = {
    val validName = """^([a-z][a-z0-9_$()+/-]*)$""".r
    dbName match {
      case validName(name) => new DatabaseName(escapeSlash(name))
      case _ =>
        throw new IllegalArgumentException( s""" "$dbName" is an invalid database name, must match pattern "${validName.toString()}" """)
    }
  }

  private def escapeSlash(s: String) = s.replaceAllLiterally("/", "%2F2")

  implicit class StringWithToDatabaseName(dbString: String) {
    def asDatabaseName: DatabaseName = valueOf(dbString)
  }

}