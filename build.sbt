name := """gubbns"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  cache,
  ws,
  "org.specs2" %% "specs2" % "2.3.12" % "test",
  "org.mockito" % "mockito-core" % "1.9.5" % "test",
  "org.pegdown" % "pegdown" % "1.4.2"
)

scalacOptions ++= Seq("-unchecked", "-deprecation")

LessKeys.compress in Assets := true