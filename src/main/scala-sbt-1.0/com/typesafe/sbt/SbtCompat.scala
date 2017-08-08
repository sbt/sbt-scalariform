package com.typesafe.sbt

import java.io.File
import sbt.FileInfo
import sbt.util.{CacheStore, CacheStoreFactory, Tracked}
import sjsonnew.JsonFormat

object SbtCompat {
  val Analysis = sbt.internal.inc.Analysis
  type UpdateFunction = sbt.FileFunction.UpdateFunction

  object FileFunction {
    def cached(cacheBaseDirectory: File)(inStyle: FileInfo.Style, outStyle: FileInfo.Style)(action: UpdateFunction): Set[File] => Set[File] =
      sbt.FileFunction.cached(CacheStoreFactory(cacheBaseDirectory), inStyle, outStyle)(action)
  }

  def changed[O: Equiv: JsonFormat](store: File) = {
    new Changed[O](CacheStore(store))
  }

  private[sbt] class Changed[O: Equiv: JsonFormat](val store: CacheStore) extends Tracked {
    def clean() = store.delete()

    def apply[O2](ifChanged: O => O2, ifUnchanged: O => O2): O => O2 = value => {
      if (uptodate(value)) ifUnchanged(value)
      else {
        update(value)
        ifChanged(value)
      }
    }

    def update(value: O): Unit = store.write(value)
    def uptodate(value: O): Boolean = {
      val equiv: Equiv[O] = implicitly
      try { equiv.equiv(value, store.read[O]) }
      catch { case _: Exception => false }
    }
  }
}
