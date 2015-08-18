import sbt._
import sbt.Keys._
import sbt.{ThisBuild, Project}
import scala.Some
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._

object SbtScalariformBuild extends Build {

  val formattingPreferences = {
    import scalariform.formatter.preferences._
    FormattingPreferences()
      .setPreference(PreserveDanglingCloseParenthesis, true)
      .setPreference(AlignParameters, true)
      .setPreference(CompactStringConcatenation, true)
      .setPreference(CompactControlReadability, false)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 40)
      .setPreference(SpacesWithinPatternBinders, true)
      .setPreference(DoubleIndentClassDeclaration, true)
  }

  def getPublishToRepo(isSnapshot: Boolean) =
    if (isSnapshot)
      Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")
    else
      Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")

  val sbtScalariform: Project = Project(
    "sbt-scalariform",
    file("."),
    settings = Defaults.defaultSettings ++ SbtScalariform.scalariformSettings ++ Seq(
      organization := "org.scalariform",
      name := "sbt-scalariform",
      version in ThisBuild := "1.5.0",
      resolvers ++= Resolvers.resolvers,
      libraryDependencies ++= Dependencies.sbtScalariform,
      scalacOptions ++= List(
        "-unchecked",
        "-deprecation",
        "-Xlint",
        "-language:_",
        "-target:jvm-1.6",
        "-encoding", "UTF-8"
      ),
      ScalariformKeys.preferences := formattingPreferences,
      sbtPlugin := true,
      publishTo <<= isSnapshot(getPublishToRepo),
      publishMavenStyle := true,
      publishArtifact in Test := false,
      publishArtifact in (Compile, packageSrc) := true,
      pomExtra :=
        <url>http://github.com/daniel-trinh/sbt-scalariform</url>
        <licenses>
          <license>
            <name>Apache 2.0</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@github.com:daniel-trinh/sbt-scalariform.git</url>
          <connection>scm:git@github.com:daniel-trinh/sbt-scalariform.git</connection>
        </scm>
        <developers>
          <developer>
            <id>hseeberger</id>
            <name>Heiko Seeberger</name>
            <url>http://blog.heikoseeberger.name/</url>
          </developer>
          <developer>
            <id>daniel-trinh</id>
            <name>Daniel Trinh</name>
            <url>http://danieltrinh.com</url>
          </developer>
        </developers>
    )
  )
}
object Resolvers {
  val sonatypeSnapshots = "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  val sonatypeReleases = "Sonatype OSS Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"

  val resolvers = Seq(
    sonatypeSnapshots,
    sonatypeReleases
  )
}