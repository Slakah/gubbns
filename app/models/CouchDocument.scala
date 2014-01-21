package models


trait CouchDocument {
  def _id: String
  def _rev: String
}
