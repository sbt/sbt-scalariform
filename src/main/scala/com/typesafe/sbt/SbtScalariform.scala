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
import sbt.{ IntegrationTest => It }
import sbt.Keys._
import scala.collection.immutable.Seq
import scalariform.formatter.preferences.IFormattingPreferences

object SbtScalariform extends AutoPlugin {
  override def requires = plugins.JvmPlugin
  override def trigger = allRequirements

  object autoImport {
    val scalariformFormat = taskKey[Seq[File]]("Format (Scala) sources using scalariform")
    val scalariformPreferences = settingKey[IFormattingPreferences]("Scalariform formatting preferences, e.g. indentation")

    def scalariformSettings: Seq[Setting[_]] = Def.settings(
      defaultScalariformSettings,
      compileInputs in (Compile, compile) := ((compileInputs in (Compile, compile)) dependsOn (scalariformFormat in Compile)).value,
      compileInputs in (Test, compile) := ((compileInputs in (Test, compile)) dependsOn (scalariformFormat in Test)).value
    ).toList
  }
  import autoImport._

  override def projectSettings = scalariformSettings

  val defaultPreferences = {
    import scalariform.formatter.preferences._
    FormattingPreferences()
  }

  object ScalariformKeys {

    val format = autoImport.scalariformFormat

    val preferences = autoImport.scalariformPreferences
  }

  def scalariformSettings: Seq[Setting[_]] = autoImport.scalariformSettings

  def scalariformSettingsWithIt: Seq[Setting[_]] = Def.settings(
    defaultScalariformSettingsWithIt,
    compileInputs in (Compile, compile) := ((compileInputs in (Compile, compile)) dependsOn (scalariformFormat in Compile)).value,
    compileInputs in (Test, compile) := ((compileInputs in (Test, compile)) dependsOn (scalariformFormat in Test)).value,
    compileInputs in (It, compile) := ((compileInputs in (It, compile)) dependsOn (scalariformFormat in It)).value
  ).toList

  def defaultScalariformSettings: Seq[Setting[_]] =
    formatOnDemandSettings ++ inConfig(Compile)(configScalariformSettings) ++ inConfig(Test)(configScalariformSettings)

  def defaultScalariformSettingsWithIt: Seq[Setting[_]] =
    defaultScalariformSettings ++ inConfig(It)(configScalariformSettings)

  def configScalariformSettings: Seq[Setting[_]] =
    List(
      (sourceDirectories in Global in scalariformFormat) := List(scalaSource.value),
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

  def formatOnDemandSettings: Seq[Setting[_]] =
    List(
      scalariformPreferences in Global := defaultPreferences,
      includeFilter in Global in scalariformFormat := "*.scala"
    )
}
