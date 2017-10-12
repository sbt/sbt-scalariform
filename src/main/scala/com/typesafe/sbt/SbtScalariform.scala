/*
 * Copyright 2011-2012 Typesafe Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.typesafe.sbt

import sbt._
import sbt.{IntegrationTest => It}
import sbt.Keys._
import scala.collection.immutable.Seq
import scalariform.formatter.preferences.{
  IFormattingPreferences,
  FormattingPreferences
}

object SbtScalariform
  extends AutoPlugin {

  override def requires = plugins.JvmPlugin
  override def trigger = allRequirements

  object autoImport {
    val scalariformFormat = taskKey[Seq[File]]("Format (Scala) sources using scalariform")
    val scalariformPreferences = settingKey[IFormattingPreferences]("Scalariform formatting preferences")
    val scalariformAutoformat = settingKey[Boolean]("Whether Scala sources should be auto formatted when compile is run (default: true)")
    val scalariformDoAutoformat = taskKey[Seq[File]]("Format sources if autoformat is configured")
    val scalariformWithBaseDirectory = settingKey[Boolean]("Whether or not to format sources in project root (default: false)")

    @deprecated("Use scalariformAutoformat to turn autoformat on or off", "1.8.1")
    def scalariformSettings(autoformat: Boolean): Seq[Setting[_]] = Nil

    @deprecated("Use scalariformItSettings", "1.8.1")
    def scalariformSettingsWithIt(autoformat: Boolean): Seq[Setting[_]] = scalariformItSettings

    def scalariformItSettings: Seq[Setting[_]] = inputs(It) ++ inConfig(It)(configScalariformSettings)
  }
  import autoImport._

  override def globalSettings = Seq(
    scalariformPreferences := defaultPreferences,
    includeFilter in scalariformFormat := "*.scala",
    scalariformAutoformat := (
      PreferencesFile(None).map(_.autoformat.autoformat).getOrElse(Defaults.autoformat)
    ),
    scalariformWithBaseDirectory := (
      PreferencesFile(None).map(_.withBaseDirectory).getOrElse(Defaults.withBaseDirectory)
    )
  )

  override def projectSettings = compileSettings ++
    inConfig(Compile)(configScalariformSettings) ++
    inConfig(Test)(configScalariformSettings)

  object ScalariformKeys {
    val format = scalariformFormat
    val preferences = scalariformPreferences
    val autoformat = scalariformAutoformat
    val withBaseDirectory = scalariformWithBaseDirectory
  }

  private[sbt] object Defaults {
    val autoformat = true
    val withBaseDirectory = false
  }

  val defaultPreferences = FormattingPreferences()

  private val compileSettings = inputs(Compile) ++ inputs(Test)

  private def inputs(config: Configuration): Seq[Setting[_]] = {
    val input = compileInputs in (config, compile)
    Seq(
      input := (input dependsOn (scalariformDoAutoformat in config)).value
    )
  }

  def configScalariformSettings: Seq[Setting[_]] = {
    List(
      (sourceDirectories in scalariformFormat) :=
        unmanagedSourceDirectories.value ++ (
          if (!scalariformWithBaseDirectory.value) Seq.empty
          else Seq(baseDirectory.in(LocalRootProject).value)
        ),
      scalariformPreferences := getPreferences(scalariformPreferences.value)(None),
      scalariformFormat := Scalariform(
        getPreferences(scalariformPreferences.value)(Some(streams.value)),
        (sourceDirectories in scalariformFormat).value.toList,
        (includeFilter in scalariformFormat).value,
        (excludeFilter in scalariformFormat).value,
        thisProjectRef.value,
        configuration.value,
        streams.value,
        scalaVersion.value
      ),
      scalariformDoAutoformat := Def.taskDyn {
        if (scalariformAutoformat.value) {
          Def.task {
            scalariformFormat.value
          }
        }
        else {
          Def.task {
            Seq.empty[File]
          }
        }
      }.value
    )
  }

  private val getPreferences = (buildPrefs: IFormattingPreferences) =>
    (PreferencesFile(_)).
      andThen(_.flatMap(_.prefs).getOrElse(
        buildPrefs
      ))

  // DEPRECATIONS
  //
  @deprecated("use scalariformSettings(autoformat: Boolean)", "1.8.0")
  def scalariformSettings: Seq[Setting[_]] = {
    autoImport.scalariformSettings(autoformat = true)
  }

  @deprecated("use scalariformSettingsWithIt(autoformat: Boolean)", "1.8.0")
  def scalariformSettingsWithIt: Seq[Setting[_]] = {
    autoImport.scalariformSettingsWithIt(autoformat = true)
  }

  @deprecated("use scalariformSettings(autoformat: Boolean)", "1.8.0")
  def defaultScalariformSettings: Seq[Setting[_]] =
    autoImport.scalariformSettings(autoformat = true)

  @deprecated("use scalariformSettingsWithIt(autoformat: Boolean)", "1.8.0")
  def defaultScalariformSettingsWithIt: Seq[Setting[_]] =
    autoImport.scalariformSettingsWithIt(autoformat = true)
}
