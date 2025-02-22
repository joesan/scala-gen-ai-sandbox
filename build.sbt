
lazy val root = project.in(file("."))
  .aggregate(genAITokenize)
  .settings(
     name := "scala-gen-ai-sandbox",
     description := "Sandbox to experiment with some Gen AI related topics",
     // crossScalaVersions must be set to Nil on the aggregating project
     crossScalaVersions := Nil,
     publish / skip := true
  )

lazy val genAITokenize = project.in(file("gen-ai-tokenize")) //.enablePlugins(PlayScala)
