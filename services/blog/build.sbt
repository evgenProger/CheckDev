import Dependencies._



lazy val commonSettings = Seq (
  organization := "ru.checkdev",
  name := "blog",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.6"
)

lazy val blog = (project in file("."))
  .enablePlugins(ScalatraPlugin, SbtTwirl)
  .disablePlugins(ScalastylePlugin)
  .settings(
    commonSettings,
    libraryDependencies ++= dependencies
  )
javaOptions ++= Seq(
  "-Xdebug",
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
)


