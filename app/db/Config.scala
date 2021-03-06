package db

import com.google.inject.ImplementedBy
import play.api.Play


case class Config(
                   host: String = "localhost",
                   port: Int = 5984,
                   protocol: String = "http") {
  def this(host: Option[String], port: Option[Int]) {
    this(host.getOrElse("localhost"), port.getOrElse(5984))
  }
}

@ImplementedBy(classOf[PlayConfigService])
trait ConfigService {
  def config(): Config
}


class PlayConfigService extends ConfigService {
  override def config() = {
    val confKey = "couchdb.default"

    val playConf = Play.current.configuration

    new Config(
      host = playConf.getString(s"$confKey.host"),
      port = playConf.getInt(s"$confKey.port")
    )
  }
}