val projectName = "sbt-scalariform"
val sbtScalariform = Project(projectName, file("."))
           sbtPlugin := true
        organization := "org.scalariform"
                name := projectName
 sonatypeProfileName := organization.value
version in ThisBuild := "1.8.3"

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

crossSbtVersions := Vector("0.13.18", "1.2.8")

scalacOptions ++= List(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-language:_",
  "-encoding", "UTF-8"
) ++ (if (scalaVersion.value startsWith "2.10.") List("-target:jvm-1.6") else List.empty)

libraryDependencies += "org.scalariform" %% "scalariform" % "0.2.10"

com.typesafe.sbt.SbtScalariform.ScalariformKeys.preferences := {
  import scalariform.formatter.preferences._
  FormattingPreferences()
    .setPreference(AlignArguments, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 20)
    .setPreference(DanglingCloseParenthesis, Force)
    .setPreference(CompactControlReadability, true)
    .setPreference(SpacesAroundMultiImports, false)
}

// preserve formatting of sbt-scripted test files
excludeFilter in scalariformFormat := "unformatted.scala" || "formatted.scala"

enablePlugins(SbtPlugin)
scriptedLaunchOpts := {
  val sbtAssemblyVersion = "0.14.5"

  scriptedLaunchOpts.value ++ Seq(
    "-Xmx1024M",
    s"-Dsbt-assembly.version=${sbtAssemblyVersion}",
    s"-Dsbt-scalariform.version=${version.value}"
  )
}
scriptedBufferLog := false

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
