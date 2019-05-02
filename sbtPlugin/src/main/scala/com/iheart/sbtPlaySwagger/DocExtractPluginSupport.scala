package com.iheart.sbtPlaySwagger

import com.sohoffice.doc.extract.sbt.{ DocExtractKeys, DocExtractPlugin }
import com.sohoffice.playSwagger.DocExtractPluginVersion
import sbt.Keys._
import sbt._
import sbt.AutoPlugin

object DocExtractPluginSupport extends AutoPlugin {

  object swaggerKeys extends SwaggerKeys

  import swaggerKeys._

  object docExtractKeys extends DocExtractKeys

  import docExtractKeys._

  def docExtractSettings: Seq[Setting[_]] = Seq(
    swaggerDescriptionFile := docExtractTargetFile.value.right.toOption,
    swagger := swagger.dependsOn(docExtract).value,
    resolvers += Resolver.bintrayIvyRepo("sohoffice", "sbt-plugins"),
    addSbtPlugin("com.sohoffice" % "sbt-doc-extract" % DocExtractPluginVersion.version))
}
