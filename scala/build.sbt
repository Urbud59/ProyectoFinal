name := "SistemaGestion"

version := "0.1"

scalaVersion := "2.13.12"

Compile / run / mainClass := Some("app.Main")

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "upickle" % "3.1.3",
  "com.lihaoyi" %% "cask" % "0.9.1"
)

import sbtassembly.AssemblyPlugin.autoImport._

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}
