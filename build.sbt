import play.Project._

name := """blog"""

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.2.0", 
  "org.webjars" % "bootstrap" % "3.0.0",
  "org.ektorp" % "org.ektorp" % "1.4.1",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.3.0",
  "com.fasterxml.jackson.datatype" % "jackson-datatype-joda" % "2.3.0",
  "org.apache.httpcomponents" % "httpclient" % "4.3.1"
)

playScalaSettings