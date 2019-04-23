val root = (project in file(".")).enablePlugins(PlayScala)
name := "network-label"
version := "1.0.0"
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  jdbc,
  "org.postgresql" % "postgresql" % "9.4.1208.jre7",
  "com.typesafe.play" %% "anorm" % "2.5.0",
  "org.julienrf" %% "play-jsmessages" % "2.0.0",

  "net.sf.jasperreports" % "jasperreports" % "6.4.1",
  "com.lowagie" % "itext" % "2.1.7",
  "org.olap4j" % "olap4j" % "1.2.0",
  "net.sf.barcode4j" % "barcode4j" % "2.1",
  "org.apache.xmlgraphics" % "batik-bridge" % "1.10",

  "org.webjars" % "jquery" % "3.1.0",
  "org.webjars.npm" % "semantic-ui-css" % "2.3.2"
)