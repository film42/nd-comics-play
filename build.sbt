name := "helloworld"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.jsoup" % "jsoup" % "1.7.3"
)     

play.Project.playScalaSettings
