val scala3Version = "3.6.3"

lazy val genAITokenize = project
  .in(file("."))
  .settings(
    name := "Scala 3 Project Template",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
      "org.scalatest" %% "scalatest" % "3.2.9" % Test
    )
  )
