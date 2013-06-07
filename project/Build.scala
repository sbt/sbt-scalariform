import sbt._
import sbt.Keys._
//import com.typesafe.sbt.SbtScalariform._
//import sbtrelease.ReleasePlugin._

object Build extends Build {

  lazy val root = Project(
    "sbt-scalariform",
    file("."),
    settings = Defaults.defaultSettings ++ 
//      scalariformSettings ++
//      releaseSettings ++
      Seq(
        organization := "com.typesafe.sbt",
        // version is defined in version.sbt in order to support sbt-release
        // scalaVersion := "2.9.2",
        scalacOptions ++= Seq("-unchecked", "-deprecation"),
        publishTo <<= isSnapshot(if (_) Some(Classpaths.sbtPluginSnapshots) else Some(Classpaths.sbtPluginReleases)),
        sbtPlugin := true,
        publishMavenStyle := false,
        publishArtifact in (Compile, packageDoc) := false,
        publishArtifact in (Compile, packageSrc) := false,
        libraryDependencies ++= Seq(
          Dependencies.Compile.Scalariform
        ),
        initialCommands in console := "import com.typesafe.sbt.scalariform._"
      )
  )

  object Dependencies {

    object Compile {
      val Scalariform = "org.scalariform" %% "scalariform" % "0.1.4"
    }
  }
}
