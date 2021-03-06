
lazy val commonRootSettings = Seq(
  organization := "com.rallyhealth",
  organizationName := "Rally Health",
  version := "1.3.2",
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

  resolvers += "jeffmay at bintray" at "https://dl.bintray.com/jeffmay/maven",

  libraryDependencies ++= Seq(
    "joda-time" % "joda-time" % "2.8",
    "org.joda" % "joda-convert" % "1.7",
    "org.scalacheck" %% "scalacheck" % "1.11.3" % "test",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "me.jeffmay" %% "scalacheck-ops" % "1.1.0" % "test"
  ),

  // disable compilation of ScalaDocs, since this always breaks on links
  sources in(Compile, doc) := Seq.empty,

  // disable publishing empty ScalaDocs
  publishArtifact in (Compile, packageDoc) := false,

  licenses += ("Apache-2.0", url("http://opensource.org/licenses/apache-2.0"))

)

lazy val core = (project in file("bson-adt-core")).settings(common).settings(
  name := "bson-adt-core",
  libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-functional" % "2.4.0-M2",
    "org.mongodb" % "bson" % "3.0.0",
    "junit" % "junit" % "4.12" % "test"
  )
)

lazy val mongo2 = (project in file("bson-adt-mongo2")).settings(common).settings(
  name := "bson-adt-mongo2",
  libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-functional" % "2.4.0-M2",
    "org.mongodb" % "mongo-java-driver" % "2.13.1"
  )
).dependsOn(
    core % "compile->compile;test->test"
  )

lazy val casbah = (project in file("bson-adt-casbah")).settings(common).settings(
  name := "bson-adt-casbah",
  libraryDependencies ++= Seq(
    "org.mongodb" %% "casbah-core" % "2.8.0",
    "org.mongodb" %% "casbah-query" % "2.8.0"
  )
).dependsOn(
    mongo2 % "compile->compile;test->test"
  )

lazy val mongo = (project in file("bson-adt-mongo3")).settings(common).settings(
  name := "bson-adt-mongo",
  libraryDependencies ++= Seq(
    "org.mongodb" % "mongodb-driver-core" % "3.0.2",
    "org.mongodb" % "mongodb-driver" % "3.0.2"
  ),
  testOptions in Test += Tests.Cleanup {
    (loader: java.lang.ClassLoader) =>
      println("Running test cleanup for bson-adt-mongo project...")
      loader.loadClass("adt.bson.test.Cleanup").newInstance
  }
).dependsOn(
    core % "compile->compile;test->test"
  )

lazy val `mongo-async` = (project in file("bson-adt-mongo3-async")).settings(common).settings(
  name := "bson-adt-mongo-async",
  libraryDependencies ++= Seq(
    "org.mongodb" % "mongodb-driver-core" % "3.0.2",
    "org.mongodb" % "mongodb-driver-async" % "3.0.2"
  ),
  testOptions in Test += Tests.Cleanup {
    (loader: java.lang.ClassLoader) =>
      println("Running test cleanup for bson-adt-mongo-async project...")
      loader.loadClass("adt.bson.test.Cleanup").newInstance
  }
).dependsOn(
    core % "compile->compile;test->test"
  )

