val sbtScalariform = Project("sbt-scalariform", file("."))

        organization := "org.scalariform"
                name := "sbt-scalariform"
version in ThisBuild := "1.6.0"

  licenses := Seq(("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0")))
  homepage := scmInfo.value map (_.browseUrl)
   scmInfo := Some(ScmInfo(url("https://github.com/sbt/sbt-scalariform"), "scm:git:git@github.com:sbt/sbt-scalariform.git"))
developers := List(
  Developer("hseeberger", "Heiko Seeberger", "mail at heikoseeberger de", url("http://blog.heikoseeberger.name/")),
  Developer("daniel-trinh", "Daniel Trinh", "daniel s trinh at gmail com", url("http://danieltrinh.com"))
)

sbtPlugin := true

scalacOptions ++= List(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-language:_",
  "-target:jvm-1.6",
  "-encoding", "UTF-8"
)

resolvers ++= Seq(sonatypeSnapshots, sonatypeReleases)

libraryDependencies += "org.scalariform" %% "scalariform" % "0.2.0"

com.typesafe.sbt.SbtScalariform.ScalariformKeys.preferences := {
  import scalariform.formatter.preferences._
  FormattingPreferences()
    .setPreference(AlignParameters, true)
    .setPreference(CompactStringConcatenation, true)
    .setPreference(CompactControlReadability, false)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 40)
    .setPreference(SpacesWithinPatternBinders, true)
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(SpacesAroundMultiImports, true)
    .setPreference(DanglingCloseParenthesis, Preserve)
}

publishTo := Some(if (isSnapshot.value) sonatypeSnapshots else sonatypeReleases)
publishMavenStyle := true
publishArtifact in Test := false
publishArtifact in (Compile, packageSrc) := true

val sonatypeSnapshots = "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
val sonatypeReleases  = "Sonatype OSS Releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
