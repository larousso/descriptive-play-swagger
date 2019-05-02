package com.iheart.playSwagger

import java.io.File
import java.nio.file.{ Files, Paths, StandardOpenOption }

import play.api.libs.json.{ JsValue, Json }

import scala.util.{ Failure, Success, Try }

object SwaggerSpecRunner extends App {
  implicit def cl: ClassLoader = getClass.getClassLoader

  val targetFile :: routesFile :: domainNameSpaceArgs :: outputTransformersArgs :: swaggerV3String :: apiVersion :: swaggerPrettyJson :: Nil = args
    .toList.take(7)
  // read the --description-file options
  parseDescriptionFileOption(new SwaggerSpecRunnerOptions, args.drop(7).toList) match {
    case SwaggerSpecRunnerOptions(Some(descriptionFile)) ⇒
      Descriptions.useDescriptionFile(new File(descriptionFile))
    case _ ⇒ // use default description provider when option is not supplied
  }

  private def fileArg = Paths.get(targetFile)
  private def swaggerJson = {
    val swaggerV3 = java.lang.Boolean.parseBoolean(swaggerV3String)
    val domainModelQualifier = PrefixDomainModelQualifier(domainNameSpaceArgs.split(","): _*)
    val transformersStrs: Seq[String] = if (outputTransformersArgs.isEmpty) Seq() else outputTransformersArgs.split(",")
    val transformers = transformersStrs.map { clazz ⇒
      Try(cl.loadClass(clazz).asSubclass(classOf[OutputTransformer]).newInstance()) match {
        case Failure(ex: ClassCastException) ⇒
          throw new IllegalArgumentException("Transformer should be a subclass of com.iheart.playSwagger.OutputTransformer:" + clazz, ex)
        case Failure(ex) ⇒ throw new IllegalArgumentException("Could not create transformer", ex)
        case Success(el) ⇒ el
      }
    }
    val swaggerSpec: JsValue = SwaggerSpecGenerator(
      domainModelQualifier,
      outputTransformers = transformers,
      swaggerV3 = swaggerV3,
      apiVersion = Some(apiVersion)).generate(routesFile).get

    if (swaggerPrettyJson.toBoolean) Json.prettyPrint(swaggerSpec)
    else swaggerSpec.toString
  }

  Files.write(fileArg, swaggerJson.getBytes, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)

  def parseDescriptionFileOption(options: SwaggerSpecRunnerOptions, list: List[String]): SwaggerSpecRunnerOptions = {
    list match {
      case Nil ⇒ options
      case "--description-file" :: v :: others ⇒
        val newOptions = options.copy(descriptionFile = Some(v))
        parseDescriptionFileOption(newOptions, others)
      case s :: others ⇒
        // discard normal argument, they are taken in the beginning and should not exist
        parseDescriptionFileOption(options, others)
    }
  }

  case class SwaggerSpecRunnerOptions(
    descriptionFile: Option[String] = None)
}
