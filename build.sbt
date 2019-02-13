name := """qr-svc"""
organization := "com.am"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.11"

PlayKeys.externalizeResources := false
 
libraryDependencies ++= Seq(
  guice,
  javaWs,
  ws,
  "org.apache.httpcomponents" % "httpcore" % "4.4.6",
  "org.mapstruct" % "mapstruct-processor" % "1.2.0.Final",
  "io.jsonwebtoken" % "jjwt" % "0.9.1",

  javaWs % "test",
  "org.awaitility" % "awaitility" % "2.0.0" % "test",
  "org.assertj" % "assertj-core" % "3.6.2" % "test",
  "org.mockito" % "mockito-core" % "2.10.0" % "test"
)

testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")

lazy val root = (project in file(".")).enablePlugins(PlayJava)