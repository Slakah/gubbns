package util

import org.ektorp.http.StdHttpClient
import org.ektorp.impl.{StdCouchDbInstance, StdObjectMapperFactory}
import com.fasterxml.jackson.databind.{ObjectMapper}
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object BlogDB {
  private val httpClient  = new StdHttpClient.Builder()
    .url("http://localhost:5984")
    .build()
  private val objectMapperFactory = new StdObjectMapperFactory
  val mapper = new ObjectMapper

  mapper registerModule(new JodaModule)

  objectMapperFactory setObjectMapper(mapper)

  private val dbInstance = new StdCouchDbInstance(httpClient, objectMapperFactory)
  val db = dbInstance.createConnector("blog", true)
}