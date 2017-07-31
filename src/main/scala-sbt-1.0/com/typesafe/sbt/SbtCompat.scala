package com.typesafe.sbt

import java.io.File
import sbt.{Changed, FileInfo}
import sbt.util.{CacheStore, CacheStoreFactory}
import sjsonnew.JsonFormat

object SbtCompat {
  val Analysis = sbt.internal.inc.Analysis
  type UpdateFunction = sbt.FileFunction.UpdateFunction

  object FileFunction {
    def cached(cacheBaseDirectory: File)(inStyle: FileInfo.Style, outStyle: FileInfo.Style)(action: UpdateFunction): Set[File] => Set[File] =
      sbt.FileFunction.cached(CacheStoreFactory(cacheBaseDirectory), inStyle, outStyle)(action)
  }

  def changed[O: Equiv: JsonFormat](store: File): Changed[O] = new Changed[O](CacheStore(store))
}
