name := "activemq-scala"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.12",
  "com.typesafe.akka" %% "akka-camel" % "2.3.12",
  "org.apache.activemq" % "activemq-camel" % "5.11.1",
  "org.apache.activemq" % "activemq-client" % "5.11.1"
)