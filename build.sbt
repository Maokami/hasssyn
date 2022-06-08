val scala3Version = "3.1.2"

Global / onChangedBuildSource := ReloadOnSourceChanges
lazy val root = project
  .in(file("."))
  .settings(
    name := "hasssyn",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % "0.14.1",
      "io.circe" %% "circe-generic" % "0.14.1",
      "io.circe" %% "circe-parser" % "0.14.1",
      "io.circe" %% "circe-yaml" % "0.14.1",
      "org.scalameta" %% "munit" % "0.7.29" % Test
    )
  )
