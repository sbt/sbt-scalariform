sbt-scalariform
===============

Welcome to sbt-scalariform, an <a href="https://github.com/sbt/sbt">sbt</a> plugin adding support for source code formatting using <a href="https://github.com/daniel-trinh/scalariform">Scalariform</a>.

Installing sbt-scalariform
--------------------------

sbt-scalariform is a plugin for sbt 0.13. Please make sure that you are using an appropriate sbt release. In order to download and install sbt, please refer to [sbt Getting Started Guide / Setup](http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html).

As sbt-scalariform is a plugin for sbt, it is installed like any other sbt plugin, that is by mere configuration. For details about using sbt plugins, please refer to [sbt Getting Started Guide / Using Plugins](http://www.scala-sbt.org/release/docs/Getting-Started/Using-Plugins.html).

Most probably you can skip the details and just add sbt-scalariform to your local plugin definition. Local plugins are defined in a `plugins.sbt` file in the `project/` folder of your project.

To add sbt-scalariform to your build using sbt 0.13, just add the below setting, paying attention to blank lines:

```
... // Other settings
resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.4.0")
```

sbt 0.12.x and below is not supported for this fork, see `https://github.com/sbt/sbt-scalariform` for
older versions


After adding the sbt-scalariform plugin like this, you still have to configure it, i.e. add the relevant settings to your build definition. Please read on ...

Basic configuration
-------------------

Add the `scalariformSettings` to your local build definition file `build.sbt` or `scalariform.sbt`:

```
... // Other settings

scalariformSettings
```

This will add the task `scalariformFormat` in the scopes `compile` and `test` and additionally run this task automatically when compiling; for more control see the section *Advanced configuration* below

Now you are ready to go. Either start sbt or, if it was already started, reload the current session by executing the `reload` command. If everything worked, you should have the new command `scalariformFormat` available as well automatic formatting on `compile` and `test:compile` activated.

Using sbt-scalariform
---------------------

If you added the settings for this plugin like described above, you can either format your sources manually or automatically:

- Whenever you run the tasks `compile` or `test:compile`, your source files will be automatically formatted by Scalariform

- If you want to start formatting your source files explicitly, just run the task `scalariformFormat` or `test:scalariformFormat`

Advanced configuration
----------------------

sbt-scalariform comes with various configuration options. Changing the formatting preferences and deactivating the automatic formatting on compile are probably the most important ones and described in detail.

You can provide your own formatting preferences for Scalariform via the setting key `ScalariformKeys.preferences` which expects an instance of `IFormattingPreferences`. Make sure you import all necessary members from the package `scalariform.formatter.preferences`. Let's look at an example:

```
import scalariform.formatter.preferences._

scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(PreserveDanglingCloseParenthesis, true)
```

If you don't want sbt to automatically format your source files when the tasks `compile` or `test:compile`, just add `defaultScalariformSettings` instead of `scalariformSettings` to your build definition.

If you want to additionally enable Scalariform for your integration tests, use `scalariformSettingsWithIt` or `defaultScalariformSettingsWithIt` instead of the above.

Other useful configuration options are provided by common sbt setting keys:

- `includeFilter in format`: Defaults to **.scala*
- `excludeFilter in format`: Using the default of sbt

Contact
------------

Please contact `@daniel-trinh` on http://github.com for help

License
-------

This code is open source software licensed under the <a href="http://www.apache.org/licenses/LICENSE-2.0.html">Apache 2.0 License</a>.
