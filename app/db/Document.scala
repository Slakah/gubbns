package db

import java.util.UUID
import DocumentHelper._

case class Document(id: String, rev: String, json: String)

case class DocumentBase(id: String = newId, json: String)

private object DocumentHelper {
  def newId = UUID.randomUUID.toString
}