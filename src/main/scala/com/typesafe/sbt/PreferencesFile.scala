package com.typesafe.sbt

import java.io.{File, FileInputStream}
import java.util.Properties
import sbt._
import sbt.Keys._
import sbt.Keys.{TaskStreams => Streams}
import scalariform.formatter.preferences.{
  PreferencesImporterExporter,
  IFormattingPreferences
}, PreferencesImporterExporter.getPreferences

object PreferencesFile
  extends PreferencesChanged {

  val (userHome, projectHome) = (
    System.getProperty("user.home"), new File(".").getAbsolutePath
  )

  case class AutoFormat(autoformat: Boolean) {
    def isDisabled = autoformat == false
  }
  case class PreferenceData(
    autoformat: AutoFormat,
    withBaseDirectory: Boolean,
    prefs: Option[IFormattingPreferences]
  )

  def apply(maybeStreams: Option[Streams]): Option[PreferenceData] = {
    fileResult.zip(maybeStreams).foreach(logChanged)
    fileResult.map(_._1)
  }

  def getPrefs(path: String): Option[File] = {
    val s = path :: ".scalariform.conf" :: Nil
    Option(new File(s.mkString(File.separator))).filter(_.isFile)
  }

  private lazy val fileResult =
    getPrefs(projectHome).orElse(getPrefs(userHome)).map { file =>
      val (input, properties) = (
        new FileInputStream(file), new Properties
      )
      properties.load(input)
      input.close()

      val maybeAutoFormat = Option(properties.getProperty("autoformat"))
      val autoformat = AutoFormat(
        maybeAutoFormat.map(_.toBoolean).getOrElse(
          SbtScalariform.Defaults.autoformat
        )
      )
      val maybeWithBase = Option(properties.getProperty("withBaseDirectory"))
      val withBaseDirectory =
        maybeWithBase.map(_.toBoolean).getOrElse(
          SbtScalariform.Defaults.withBaseDirectory
        )
      val nonStyles = maybeAutoFormat ++ maybeWithBase
      val maybePrefs =
        properties.size match {
          case 0 => None
          case x if nonStyles.size == x => None
          case _ => Some(
            getPreferences(properties)
          )
        }
      (PreferenceData(autoformat, withBaseDirectory, maybePrefs), file)
    }

  private def logChanged(data: ((PreferenceData, File), Streams)): Unit = {
    data match {
      case ((PreferenceData(AutoFormat(autoformat), _, maybePrefs), file), streams) =>
        if (autoFormatChanged(streams)(autoformat)) {
          val action = if (autoformat) "enabled" else "disabled"
          streams.log.info(s"Scalariform auto formatting $action")
        }
        maybePrefs.foreach { prefs =>
          if (preferencesChanged(streams)(prefs)) {
            streams.log.info(
              s"Applying Scalariform preferences found in ${file.getAbsolutePath}"
            )
          }
        }
    }
  }
}
