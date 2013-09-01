organization := "com.typesafe.sbt"

name := "sbt-scalariform"

// version is defined in version.sbt in order to support sbt-release

libraryDependencies ++= Dependencies.sbtScalariform

scalacOptions ++= List(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-language:_",
  "-target:jvm-1.6",
  "-encoding", "UTF-8"
)

sbtPlugin := true

publishTo := {
  val baseUrl = "http://scalasbt.artifactoryonline.com/scalasbt"
  val kind = if (isSnapshot.value) "snapshots" else "releases"
  val name = s"sbt-plugin-$kind"
  Some(Resolver.url(s"publish-$name", url(s"$baseUrl/$name"))(Resolver.ivyStylePatterns))
}

publishMavenStyle := false

publishArtifact in (Compile, packageDoc) := false

publishArtifact in (Compile, packageSrc) := false
