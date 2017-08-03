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

  override def projectSettings = {
    // workaround autoplugin defaults overriding user settings
    // see https://github.com/sbt/sbt/issues/3389
    if (!processed.get) {
      autoImport.scalariformSettings(autoformat = true)
    }
    else Seq.empty
  }

  private val processed =
    new java.util.concurrent.atomic.AtomicBoolean(false)

  object autoImport {
    val scalariformFormat = taskKey[Seq[File]]("Format (Scala) sources using scalariform")
    val scalariformPreferences = settingKey[IFormattingPreferences]("Scalariform formatting preferences")

    def scalariformSettings(autoformat: Boolean): Seq[Setting[_]] = {
      defaultSettings(autoformat, withIt = false)
    }

    def scalariformSettingsWithIt(autoformat: Boolean): Seq[Setting[_]] = {
      defaultSettings(autoformat, withIt = true)
    }
  }
  import autoImport._

  object ScalariformKeys {
    val format = scalariformFormat
    val preferences = scalariformPreferences
  }

  val defaultPreferences = FormattingPreferences()

  private def defaultSettings(autoformat: Boolean, withIt: Boolean): Seq[Setting[_]] = {
    processed.set(true) // see `projectSettings`
    baseScalariformSettings ++
      compileSettings(autoformat, withIt) ++
      inConfig(Compile)(configScalariformSettings) ++
      inConfig(Test)(configScalariformSettings) ++ (
        if (withIt) inConfig(It)(configScalariformSettings) else Seq.empty
      )
  }

  def baseScalariformSettings: Seq[Setting[_]] = Seq(
    scalariformPreferences in Global := defaultPreferences,
    includeFilter in Global in scalariformFormat := "*.scala"
  )

  private def compileSettings(autoformat: Boolean, withIt: Boolean): Seq[Setting[_]] = {
    val enabled = (
      autoformat && !PreferencesFile(None).exists(_.autoformat.isDisabled)
    )
    if (!enabled) Seq.empty
    else if (withIt) compileSettings ++ inputs(It)
    else compileSettings
  }

  private val compileSettings = inputs(Compile) ++ inputs(Test)

  private def inputs(config: Configuration) = {
    val input = compileInputs in (config, compile)
    input := (input dependsOn (scalariformFormat in config)).value
  }.toList

  def configScalariformSettings: Seq[Setting[_]] = {
    List(
      (sourceDirectories in scalariformFormat) :=
        unmanagedSourceDirectories.value ++ Seq(
          baseDirectory.in(LocalRootProject).value
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
      )
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
  def defaultSettings: Seq[Setting[_]] =
    autoImport.scalariformSettings(autoformat = true)

  @deprecated("use scalariformSettingsWithIt(autoformat: Boolean)", "1.8.0")
  def defaultSettingsWithIt: Seq[Setting[_]] =
    autoImport.scalariformSettingsWithIt(autoformat = true)
}
