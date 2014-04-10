import play.Project._

name := """gubbns"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.4"


libraryDependencies ++= Seq(
  cache,
  "org.mockito" % "mockito-all" % "1.9.0",
  "org.webjars" %% "webjars-play" % "2.2.0",
  "org.webjars" % "bootstrap" % "3.0.0",
  "org.pegdown" % "pegdown" % "1.4.2"
)


playScalaSettings

