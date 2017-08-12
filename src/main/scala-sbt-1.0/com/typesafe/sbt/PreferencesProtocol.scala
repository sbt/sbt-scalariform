package com.typesafe.sbt

import java.util.Properties
import java.io.{StringReader, StringWriter}
import sjsonnew.{Builder, JsonFormat, PrimitiveFormats, Unbuilder}
import scalariform.formatter.preferences.{
  PreferencesImporterExporter,
  IFormattingPreferences
}

object PreferencesProtocol
  extends PrimitiveFormats {

  implicit object PrefFormat extends JsonFormat[IFormattingPreferences] {
    override def write[J](value: IFormattingPreferences, builder: Builder[J]): Unit = {
      val outStream = new StringWriter()
      PreferencesImporterExporter.asProperties(value).store(outStream, null)
      builder.writeString(outStream.toString)
    }

    override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): IFormattingPreferences = {
      val properties = new Properties
      jsOpt match {
        case Some(js) =>
          val str = unbuilder.readString(js)
          properties.load(new StringReader(str))
        case None =>
      }
      PreferencesImporterExporter.getPreferences(properties)
    }
  }
}
