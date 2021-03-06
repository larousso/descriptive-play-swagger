
organization in ThisBuild := "com.sohoffice"

resolvers +=  Resolver.bintrayRepo("scalaz", "releases")


lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

lazy val descriptivePlaySwagger = project.in(file("."))
  .aggregate(playSwagger, sbtPlaySwagger)
  .settings(sourcesInBase := false)
  .settings(noPublishSettings:_*)

lazy val playSwagger = project.in(file("core"))
  .settings(Publish.coreSettings ++ Format.settings ++ Testing.settings)
  .settings(
    name := "descriptive-play-swagger",
    libraryDependencies ++= Dependencies.playTest ++
      Dependencies.playRoutesCompiler ++
      Dependencies.playJson ++
      Dependencies.test ++
      Dependencies.yaml,
    scalaVersion := "2.12.4"
  )

lazy val sbtPlaySwagger = project.in(file("sbtPlugin"))
  .settings(Publish.sbtPluginSettings ++ Format.settings)
  .settings(DocExtractSupport.settings)
  .settings(
    addSbtPlugin("com.typesafe.sbt" %% "sbt-native-packager" % "1.3.17" % Provided),
    addSbtPlugin("com.typesafe.sbt" %% "sbt-web" % "1.4.3" % Provided))
  .enablePlugins(BuildInfoPlugin, SbtPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version),
    buildInfoPackage := "com.sohoffice.playSwagger",
    name := "sbt-descriptive-play-swagger",
    description := "sbt plugin for play swagger spec generation with description",
    sbtPlugin := true,
    scalaVersion := "2.12.8",
    scripted := scripted.dependsOn(publishLocal in playSwagger).evaluated,
    scriptedLaunchOpts := { scriptedLaunchOpts.value ++
      Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false
  )

