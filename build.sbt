name := """qr-svc"""
organization := "com.am"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.11"

PlayKeys.externalizeResources := false
 
libraryDependencies ++= Seq(
  guice,
  javaJpa,
  javaWs,
  ws,
  "com.h2database" % "h2" % "1.4.194",
  "mysql" % "mysql-connector-java" % "5.1.41",
  "org.apache.httpcomponents" % "httpcore" % "4.4.6",
  "org.hibernate" % "hibernate-core" % "5.4.0.Final",
  "org.mapstruct" % "mapstruct-processor" % "1.2.0.Final",

  javaWs % "test",
  "org.awaitility" % "awaitility" % "2.0.0" % "test",
  "org.assertj" % "assertj-core" % "3.6.2" % "test",
  "org.mockito" % "mockito-core" % "2.10.0" % "test"
)

testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")

lazy val root = (project in file(".")).enablePlugins(PlayJava)