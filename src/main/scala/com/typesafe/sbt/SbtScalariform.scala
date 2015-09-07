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
import sbt.plugins.JvmPlugin
import sbt.{ IntegrationTest => It }
import sbt.Keys._
import scala.collection.immutable.Seq
import scalariform.formatter.preferences.IFormattingPreferences

object SbtScalariform extends AutoPlugin {

  val defaultPreferences = {
    import scalariform.formatter.preferences._
    FormattingPreferences()
      .setPreference(SpacesAroundMultiImports, true) // this was changed in 0.1.7 scalariform, setting this to preserve default.
      .setPreference(DoubleIndentClassDeclaration, true)
  }

  object autoImport {

    val scalariformFormat: TaskKey[Seq[File]] =
      TaskKey[Seq[File]](
        prefixed("format"),
        "Format (Scala) sources using scalariform"
      )

    val scalariformPreferences: SettingKey[IFormattingPreferences] =
      SettingKey[IFormattingPreferences](
        prefixed("preferences"),
        "Scalariform formatting preferences, e.g. indentation"
      )
    private def prefixed(key: String) = s"scalariform-$key"

    def scalariformSettings: Seq[Setting[_]] =
      defaultScalariformSettings ++ List(
        compileInputs in (Compile, compile) <<= (compileInputs in (Compile, compile)) dependsOn (scalariformFormat in Compile),
        compileInputs in (Test, compile) <<= (compileInputs in (Test, compile)) dependsOn (scalariformFormat in Test)
      )
  }

  import autoImport._

  override lazy val projectSettings = scalariformSettings
  override val trigger = allRequirements
  override val requires = JvmPlugin

  object ScalariformKeys {

    val format = autoImport.scalariformFormat

    val preferences = autoImport.scalariformPreferences
  }

  def scalariformSettings: Seq[Setting[_]] = autoImport.scalariformSettings

  def scalariformSettingsWithIt: Seq[Setting[_]] =
    defaultScalariformSettingsWithIt ++ List(
      compileInputs in (Compile, compile) <<= (compileInputs in (Compile, compile)) dependsOn (scalariformFormat in Compile),
      compileInputs in (Test, compile) <<= (compileInputs in (Test, compile)) dependsOn (scalariformFormat in Test),
      compileInputs in (It, compile) <<= (compileInputs in (It, compile)) dependsOn (scalariformFormat in It)
    )

  def defaultScalariformSettings: Seq[Setting[_]] =
    noConfigScalariformSettings ++ inConfig(Compile)(configScalariformSettings) ++ inConfig(Test)(configScalariformSettings)

  def defaultScalariformSettingsWithIt: Seq[Setting[_]] =
    defaultScalariformSettings ++ inConfig(It)(configScalariformSettings)

  def configScalariformSettings: Seq[Setting[_]] =
    List(
      (sourceDirectories in scalariformFormat) := List(scalaSource.value),
      scalariformFormat := Scalariform(
        scalariformPreferences.value,
        (sourceDirectories in scalariformFormat).value.toList,
        (includeFilter in scalariformFormat).value,
        (excludeFilter in scalariformFormat).value,
        thisProjectRef.value,
        configuration.value,
        streams.value,
        scalaVersion.value
      )
    )

  def noConfigScalariformSettings: Seq[Setting[_]] =
    List(
      scalariformPreferences := defaultPreferences,
      includeFilter in scalariformFormat := "*.scala"
    )
}
