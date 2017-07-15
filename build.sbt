val projectName = "sbt-scalariform"
val sbtScalariform = Project(projectName, file("."))
           sbtPlugin := true
        organization := "org.scalariform"
                name := projectName
 sonatypeProfileName := organization.value
version in ThisBuild := "1.7.1-SNAPSHOT"
        scalaVersion := "2.10.6"

  licenses := Seq(("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0")))
  homepage := scmInfo.value map (_.browseUrl)
   scmInfo :=
     Some(
       ScmInfo(url("https://github.com/sbt/sbt-scalariform"),
      "scm:git:git@github.com:sbt/sbt-scalariform.git")
     )
  developers := List(
    Developer(
      "hseeberger", "Heiko Seeberger", "mail at heikoseeberger de",
      url("http://blog.heikoseeberger.name/")
    ),
    Developer(
      "daniel-trinh", "Daniel Trinh", "daniel s trinh at gmail com",
      url("http://danieltrinh.com")
    )
  )

scalacOptions ++= List(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-language:_",
  "-target:jvm-1.6",
  "-encoding", "UTF-8"
)

libraryDependencies += "org.scalariform" %% "scalariform" % "0.2.1"

com.typesafe.sbt.SbtScalariform.ScalariformKeys.preferences := {
  import scalariform.formatter.preferences._
  FormattingPreferences()
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DanglingCloseParenthesis, Preserve)
    .setPreference(CompactStringConcatenation, true)
    .setPreference(SpacesAroundMultiImports, true)
}

publishMavenStyle := true
publishArtifact in Test := false
publishArtifact in (Compile, packageDoc) := true
publishArtifact in (Compile, packageSrc) := true
pomIncludeRepository := { _ => false }
buildInfoKeys := Seq[BuildInfoKey](version)
buildInfoPackage := projectName
publishTo := getPublishToRepo.value
credentials ++= {
  val creds = Path.userHome / ".m2" / "credentials"
  if (creds.exists) Seq(Credentials(creds)) else Nil
}

def getPublishToRepo = Def.setting {
  if (isSnapshot.value)
    Some(Opts.resolver.sonatypeSnapshots)
  else
    Some(Opts.resolver.sonatypeStaging)
}
