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

import sbt.Keys._
import sbt.{File, FileFilter, FileFunction => _, _}
import scala.collection.immutable.Seq
import scalariform.formatter.ScalaFormatter
import scalariform.formatter.preferences.IFormattingPreferences
import scalariform.parser.ScalaParserException
import SbtCompat._

object Scalariform
  extends PreferencesChanged {

  def apply(
    preferences: IFormattingPreferences,
    sourceDirectories: Seq[File],
    includeFilter: FileFilter,
    excludeFilter: FileFilter,
    ref: ProjectRef,
    configuration: Configuration,
    streams: TaskStreams,
    scalaVersion: String
  ): Seq[File] = {

    def log(label: String, logger: Logger)(message: String)(count: String) =
      logger.info(message.format(count, label))

    def performFormat(files: Set[File]) =
      for (file <- files if file.exists) {
        try {
          val contents = IO.read(file)
          val formatted = ScalaFormatter.format(
            contents,
            preferences,
            scalaVersion = pureScalaVersion(scalaVersion)
          )
          if (formatted != contents) IO.write(file, formatted)
        }
        catch {
          case e: ScalaParserException =>
            streams.log.warn("Scalariform parser error for %s: %s".format(file, e.getMessage))
        }
      }

    val files = sourceDirectories.descendantsExcept(includeFilter, excludeFilter).get.toSet
    val cache = streams.cacheDirectory / "scalariform"
    val logFun = log("%s(%s)".format(Reference.display(ref), configuration), streams.log) _
    if (preferencesChanged(streams)(preferences)) {
      IO.delete(cache)
    }
    handleFiles(files, cache, logFun("Formatting %s %s ..."), performFormat)
    handleFiles(files, cache, logFun("Reformatted %s %s."), _ => ()).toList // recalculate cache because we're formatting in-place
  }

  def handleFiles(
    files: Set[File],
    cache: File,
    logFun: String => Unit,
    updateFun: Set[File] => Unit
  ): Set[File] = {

    def handleUpdate(in: ChangeReport[File], out: ChangeReport[File]) = {
      val files = in.modified -- in.removed
      Analysis.counted("Scala source", "", "s", files.size) foreach logFun
      updateFun(files)
      files
    }

    FileFunction.cached(cache)(FilesInfo.hash, FilesInfo.exists)(handleUpdate)(files)
  }

  def pureScalaVersion(scalaVersion: String): String =
    scalaVersion split "-" head

}
