import sbt.Keys._
import sbt.{ File, _ }

object DocExtractSupport {

  object Versions {
    val playSwagger = "0.7.6-SNAPSHOT"
    val docExtract = "0.0.4"
  }

  val writeDocExtractVersion = TaskKey[Seq[File]]("writeDocExtractVersion", "Write docExtractVersion to file")

  val settings: Seq[Setting[_]] = Seq(
    addSbtPlugin("com.sohoffice" % "sbt-doc-extract" % Versions.docExtract),
    writeDocExtractVersion := Def.task[Seq[File]] {
      val file = sourceManaged.value / "main/doc-extract-info/DocExtractPluginVersion.scala"
      IO.delete(file)

      IO.write(
        file,
        s"""package com.sohoffice.playSwagger
           |
           |case object DocExtractPluginVersion {
           |  val version: String = "${Versions.docExtract}"
           |}
         """.stripMargin)
      Seq(file)
    }.value,
    sourceGenerators in Compile += writeDocExtractVersion)

}
