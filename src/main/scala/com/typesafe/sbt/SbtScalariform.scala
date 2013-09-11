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
import sbt.Keys._
import scala.collection.immutable.Seq
import scalariform.formatter.preferences.{ FormattingPreferences, IFormattingPreferences }

object SbtScalariform extends Plugin {

  object ScalariformKeys {

    val format: TaskKey[Seq[File]] =
      TaskKey[Seq[File]](
        prefixed("format"),
        "Format (Scala) sources using scalariform"
      )

    val preferences: SettingKey[IFormattingPreferences] =
      SettingKey[IFormattingPreferences](
        prefixed("preferences"),
        "Scalariform formatting preferences, e.g. indentation"
      )

    private def prefixed(key: String) = s"scalariform${key.capitalize}"
  }

  import ScalariformKeys._

  val defaultPreferences = {
    import scalariform.formatter.preferences._
    FormattingPreferences()
      .setPreference(DoubleIndentClassDeclaration, true)
      .setPreference(PreserveDanglingCloseParenthesis, true)
  }

  def scalariformSettings: Seq[Setting[_]] =
    defaultScalariformSettings ++ List(
      compileInputs in (Compile, compile) <<= (compileInputs in (Compile, compile)) dependsOn (format in Compile),
      compileInputs in (Test, compile) <<= (compileInputs in (Test, compile)) dependsOn (format in Test)
    )

  def defaultScalariformSettings: Seq[Setting[_]] =
    noConfigScalariformSettings ++ inConfig(Compile)(configScalariformSettings) ++ inConfig(Test)(configScalariformSettings)

  def configScalariformSettings: Seq[Setting[_]] =
    List(
      (sourceDirectories in format) := List(scalaSource.value),
      format := Scalariform(
        preferences.value,
        (sourceDirectories in format).value.toList,
        (includeFilter in format).value,
        (excludeFilter in format).value,
        thisProjectRef.value,
        configuration.value,
        streams.value,
        scalaVersion.value
      )
    )

  def noConfigScalariformSettings: Seq[Setting[_]] =
    List(
      preferences := defaultPreferences,
      includeFilter in format := "*.scala"
    )
}
