
organizationName := "Rally Health"

organization := "com.rallyhealth"

name := "bson-adt"

version := "1.2.0"

crossScalaVersions := Seq("2.11.6", "2.10.4")

scalacOptions := {
  // the deprecation:false flag is only supported by scala >= 2.11.3, but needed for scala >= 2.11.0 to avoid warnings
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, scalaMinor)) if scalaMinor >= 11 =>
      // For scala versions >= 2.11.3
      Seq("-Xfatal-warnings", "-deprecation:false")
    case Some((2, scalaMinor)) if scalaMinor < 11 =>
      // For scala versions 2.10.x
      Seq("-Xfatal-warnings", "-deprecation")
  }
} ++ Seq(
  "-feature",
  "-Ywarn-dead-code",
  "-encoding", "UTF-8"
)

libraryDependencies ++= Seq(
  "org.mongodb" %% "casbah-query" % "2.8.0",
  "org.mongodb" %% "casbah-core" % "2.8.0",
  "org.mongodb" % "mongo-java-driver" % "2.13.1",
  "org.scalacheck" %% "scalacheck" % "1.11.3",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "com.typesafe.play" %% "play-functional" % "2.4.0-M2"
)

// disable compilation of ScalaDocs, since this always breaks on links
sources in(Compile, doc) := Seq.empty

// disable publishing empty ScalaDocs
publishArtifact in (Compile, packageDoc) := false

licenses += ("Apache-2.0", url("http://opensource.org/licenses/apache-2.0"))

// Rally Settings
/////////////////

publishTo <<= version { version: String =>
  val repoBaseUrl = "https://artifacts.werally.in/artifactory/"
  val (name, url) = if (version.contains("-SNAPSHOT"))
    ("libs-snapshot-local", repoBaseUrl + "libs-snapshot-local")
  else
    ("libs-release-local", repoBaseUrl + "libs-release-local")
  Some(Resolver.url(name, new URL(url))(Resolver.mavenStylePatterns))
}

// All of the published versions
resolvers += "Artifactory Libs Release" at "https://artifacts.werally.in/artifactory/libs-release"

