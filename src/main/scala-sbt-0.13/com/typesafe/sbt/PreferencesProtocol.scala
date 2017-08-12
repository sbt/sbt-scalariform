package com.typesafe.sbt

import java.util.Properties
import java.io.{StringReader, StringWriter}
import sbinary.Operations._
import sbinary.{Input, Output, Format, DefaultProtocol}
import scalariform.formatter.preferences.{
  PreferencesImporterExporter,
  IFormattingPreferences
}

object PreferencesProtocol extends DefaultProtocol {

  implicit object PrefFormat extends Format[IFormattingPreferences]() {
    override def writes(out: Output, value: IFormattingPreferences): Unit = {
      val outStream = new StringWriter()
      PreferencesImporterExporter.asProperties(value).store(outStream, null)
      write[String](out, outStream.toString)
    }

    override def reads(in: Input): IFormattingPreferences = {
      val properties = new Properties
      properties.load(new StringReader(read[String](in)))
      PreferencesImporterExporter.getPreferences(properties)
    }
  }
}
