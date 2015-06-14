import sbt._
import sbt.Keys._

lazy val commonRootSettings = Seq(
  organization := "me.jeffmay",
  organizationName := "Jeff May",
  version := "1.2.0",
  crossScalaVersions := Seq("2.11.6", "2.10.4")
)

lazy val rootSettings = commonRootSettings ++ Seq(
  name := "bson-adt",
  publish := {},
  publishLocal := {}
)

rootSettings

lazy val common = commonRootSettings ++ Seq(

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
  ),

  libraryDependencies ++= Seq(
    "joda-time" % "joda-time" % "2.8",
    "org.joda" % "joda-convert" % "1.7",
    "org.scalacheck" %% "scalacheck" % "1.11.3" % "test",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test"
  ),

  // disable compilation of ScalaDocs, since this always breaks on links
  sources in(Compile, doc) := Seq.empty,

  // disable publishing empty ScalaDocs
  publishArtifact in (Compile, packageDoc) := false,

  licenses += ("Apache-2.0", url("http://opensource.org/licenses/apache-2.0"))

) ++ bintraySettings ++ bintrayPublishSettings

lazy val `bson-adt-core` = (project in file("bson-adt-core")).settings(common).settings(
  name := "bson-adt-core",
  libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-functional" % "2.4.0-M2",
    "org.mongodb" % "mongo-java-driver" % "2.13.1"
  )
)

lazy val `bson-adt-casbah`: Project = (project in file("bson-adt-casbah")).settings(common).settings(
  name := "bson-adt-casbah",
  libraryDependencies ++= Seq(
    "org.mongodb" %% "casbah-core" % "2.8.0",
    "org.mongodb" %% "casbah-query" % "2.8.0"
  )
).dependsOn(
    `bson-adt-core` % "compile->compile;test->test"
  )

lazy val `bson-adt-rxmongo`: Project = (project in file("bson-adt-rxmongo")).settings(common).settings(
  name := "bson-adt-rxmongo",
  libraryDependencies ++= Seq(
    "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23"
  )
).dependsOn(
    `bson-adt-core` % "compile->compile;test->test"
  )

