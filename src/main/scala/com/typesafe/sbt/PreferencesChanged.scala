package com.typesafe.sbt

import sbt._
import sbt.Keys._
import scalariform.formatter.preferences.{
  IFormattingPreferences,
  PreferencesImporterExporter
}
import SbtCompat._

private[sbt] trait PreferencesChanged {
  import com.typesafe.sbt.PreferencesProtocol._

  protected implicit val prefEquivalence = new Equiv[IFormattingPreferences]() {
    override def equiv(x: IFormattingPreferences, y: IFormattingPreferences): Boolean = {
      PreferencesImporterExporter.asProperties(x) == PreferencesImporterExporter.asProperties(y)
    }
  }

  def preferencesChanged(streams: TaskStreams): IFormattingPreferences => Boolean = {
    val hasChanged = {
      val cacheDir = streams.cacheDirectory / "scalariform-preferences"
      changed[IFormattingPreferences](cacheDir)
    }
    hasChanged({ _ => true }, { _ => false })
  }

  def autoFormatChanged(streams: TaskStreams): Boolean => Boolean = {
    val hasChanged = {
      val cacheDir = streams.cacheDirectory / "scalariform-autoformat"
      changed[Boolean](cacheDir)
    }
    hasChanged({ _ => true }, { _ => false })
  }
}
