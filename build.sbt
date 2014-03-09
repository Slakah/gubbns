import play.Project._

name := """blog"""

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.2.0",
  "org.webjars" % "bootstrap" % "3.0.0",
  "org.pegdown" % "pegdown" % "1.4.2",
  "org.mockito" % "mockito-core" % "1.8.5"
)

playScalaSettings
