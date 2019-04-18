val root = (project in file(".")).enablePlugins(PlayScala)
name := "network-label"
version := "1.0.0"
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  jdbc,
  "org.postgresql" % "postgresql" % "9.4.1208.jre7",
  "com.typesafe.play" %% "anorm" % "2.5.0",
  "org.julienrf" %% "play-jsmessages" % "2.0.0",

  "org.webjars" % "jquery" % "3.1.0",
  "org.webjars.npm" % "semantic-ui-css" % "2.3.2"
)