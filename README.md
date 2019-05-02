> This is a fork of [iheartradio play-swagger](https://github.com/iheartradio/play-swagger).
> It contributes only a minor portion to the plugin, all credits go to [iheartradio](https://github.com/iheartradio).

# Descriptive Swagger API spec generator for Play

An extension to [play-swagger](https://github.com/iheartradio/play-swagger) with the ability to show scaladoc as descriptions in the 
generated swagger spec.

## Motivation

play-swagger is cool. It allows you to work on your play framework source files and swagger spec will be generated automatically. 
Following in the DRY principle, you work in one part of your application and not only the application works, but also the API is documented.

The only downside is:

> The generated swagger spec does not have the support of descriptions.
>
> It will be great if we can `extract the scaladoc as the descriptions` for definition classes or api parameters !

I then dive into the source codes and realized play-swagger is basically built on reflection. It will have a hard time to extract documents
from source files, if this is ultimately possible. To remedy this, I've built [docExtract](https://github.com/sohoffice/docExtract). 
A scala doclet to extract documentation from your source files and it can be used to feed play-swagger.

To make the above happen, play-swagger will have to accept description feed as an external file. I've discussed with the maintainer of 
play-swagger, it may be too pre-mature to add another external file. So this project is forked, and docExtract was also integrated.

The usefulness of this plugin is still uncertain. But I'm happy using it to facilitate `API first development principle` for my new projects.  
 
## Usage

This plugin is an extension to [iheartradio play-swagger](https://github.com/iheartradio/play-swagger), please refer it for usage 
documentation. Any new upstream release will be merged back, and the release version will match.

## Prerequisite

Unlike the [iheartradio play-swagger](https://github.com/iheartradio/play-swagger), this extension supports only scala source files. It also 
requires scala 2.12 and sbt 1.0+ to run. Only Play 2.7 is supported. 

## Installation

Follow the below steps or check the [seed project](https://github.com/sohoffice/play-doc-gen-seed-projects) for reference.

- In the `project/plugins.sbt` file

```sbtshell
addSbtPlugin("com.sohoffice" %% "sbt-descriptive-play-swagger" % "0.7.5")
```

- In the `build.sbt` file

```sbtshell
resolvers += Resolver.bintrayIvyRepo("sohoffice", "sbt-plugins")

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, SwaggerPlugin)
  .settings(
    // Make sure you set the swaggerDomainNameSpaces according to your package structure.
    // You'll need this setting, otherwise swagger will fail.
    // 
    // swaggerDomainNameSpaces := Seq("io")
  )
```
  
> Swagger task will be executed in the run stage, or execute `swagger` to manually re-generate swagger.json

## Contributions

Please consider to contribute to [iheartradio play-swagger](https://github.com/iheartradio/play-swagger). I'll merge the changes back to this 
repo whenever they release a new version.

----

sohoffice, happy coding ~
