val root = (project in file(".")).enablePlugins(PlayScala)
name := "network-label"
version := "1.0.0"
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.webjars" % "jquery" % "3.1.0",
  "org.webjars.npm" % "semantic-ui-css" % "2.3.2"
)