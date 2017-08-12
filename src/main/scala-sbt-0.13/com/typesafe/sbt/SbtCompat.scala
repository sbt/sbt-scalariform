package com.typesafe.sbt

import java.io.File
import sbt.{Changed, FilesInfo}
import sbinary.Format

object SbtCompat {
  val Analysis = sbt.inc.Analysis
  type UpdateFunction = sbt.FileFunction.UpdateFunction

  object FileFunction {
    def cached(cacheBaseDirectory: File)(inStyle: FilesInfo.Style, outStyle: FilesInfo.Style)(action: UpdateFunction): Set[File] => Set[File] =
      sbt.FileFunction.cached(cacheBaseDirectory)(inStyle, outStyle)(action)
  }

  def changed[O: Equiv: Format](cacheFile: File): Changed[O] = new Changed[O](cacheFile)
}
