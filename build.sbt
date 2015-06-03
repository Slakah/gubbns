name := """gubbns"""

version := "1.0.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
  cache,
  ws,
  filters,
  "org.specs2" %% "specs2" % "2.3.12" % "test",
  "org.mockito" % "mockito-core" % "1.9.5" % "test",
  "org.pegdown" % "pegdown" % "1.4.2",
  "com.github.t3hnar" %% "scala-bcrypt" % "2.4"
)

scalacOptions ++= Seq("-unchecked", "-deprecation")

LessKeys.compress in Assets := true

// rpm packaging

enablePlugins(JavaServerAppPackaging)

{
  import com.typesafe.sbt.packager.archetypes.ServerLoader.Systemd
  serverLoading in Rpm := Systemd
}

maintainer in Linux := "James Collier"

packageSummary in Linux := "Simple hobby blogging website"

packageDescription := "Simple hobby blogging website"

rpmVendor := "gubbns"

rpmGroup := Some("Applications/System")

rpmLicense := Some("ASL 2.0")

rpmBrpJavaRepackJars := true
