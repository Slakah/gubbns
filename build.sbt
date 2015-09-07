name := """gubbns"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache,
  ws,
  filters,
  specs2 % Test,
  "org.mockito" % "mockito-core" % "1.9.5" % Test,
  "org.pegdown" % "pegdown" % "1.4.2",
  "com.github.t3hnar" %% "scala-bcrypt" % "2.4"
)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

scalacOptions ++= Seq("-unchecked", "-deprecation")

LessKeys.compress in Assets := true
