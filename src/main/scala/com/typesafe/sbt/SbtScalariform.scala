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
import scalariform.formatter.preferences.{ FormattingPreferences, IFormattingPreferences }

object SbtScalariform extends Plugin {

  def scalariformSettings: Seq[Setting[_]] = {
    import ScalariformKeys._
    defaultScalariformSettings ++ Seq(
      compileInputs in (Compile, compile) <<= (compileInputs in (Compile, compile)) dependsOn (format in Compile),
      compileInputs in (Test, compile) <<= (compileInputs in (Test, compile)) dependsOn (format in Test)
    )
  }

  def defaultScalariformSettings: Seq[Setting[_]] = {
    import Scalariform._
    val needToBeScoped = needToBeScopedScalariformSettings
    noNeedToBeScopedScalariformSettings ++ inConfig(Compile)(needToBeScoped) ++ inConfig(Test)(needToBeScoped)
  }

  object ScalariformKeys {

    val format: TaskKey[Seq[File]] =
      TaskKey[Seq[File]](
        prefix("format"),
        "Format (Scala) sources using scalariform"
      )

    val preferences: SettingKey[IFormattingPreferences] =
      SettingKey[IFormattingPreferences](
        prefix("preferences"),
        "Scalariform formatting preferences, e.g. indentation"
      )

    private def prefix(key: String) = "scalariform-" + key
  }
}
