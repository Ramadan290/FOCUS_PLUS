name := "FocusPlus"

organization := "FocusPlus.com"

version := "1.0-SNAPSHOT"

// Enables Play Framework's Scala plugin
lazy val root = (project in file(".")).enablePlugins(PlayScala)

// Scala version
scalaVersion := "2.13.15"

// Dependencies
libraryDependencies ++= Seq(
  guice, // Dependency injection
  "com.typesafe.play" %% "play-json" % "2.10.0-RC1", // Play JSON for serialization/deserialization
  "org.mongodb.scala" %% "mongo-scala-driver" % "4.9.0", // MongoDB Scala driver (updated to latest version)
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test // ScalaTest for Play (testing)
)

// Adds additional packages into Twirl templates
TwirlKeys.templateImports ++= Seq(
  "models._"
)

// Adds additional packages into conf/routes
play.sbt.routes.RoutesKeys.routesImport ++= Seq(
  "models._"
)
