sbt-scalariform
===============

Welcome to sbt-scalariform, an <a href="https://github.com/sbt/sbt">sbt</a> plugin adding support for source code formatting using <a href="https://github.com/daniel-trinh/scalariform">Scalariform</a>.

Installing sbt-scalariform
--------------------------

As of sbt-scalariform 1.5.0, this plugin is now an auto plugin. Make sure to read the _Upgrading from 1.4.0_ 
and _Advanced Configuration_ sections carefully when upgrading.

sbt-scalariform is a plugin for sbt 0.13.5+. Please make sure that you are using an appropriate sbt release.
In order to download and install sbt, please refer to [sbt Getting Started Guide / Setup](http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html).

As sbt-scalariform is a plugin for sbt, it is installed like any other sbt plugin, that is by mere configuration.
For details about using sbt plugins, please refer to [sbt Getting Started Guide / Using Plugins](http://www.scala-sbt.org/release/docs/Getting-Started/Using-Plugins.html).

If you've got a simple one project build, you can probably skip the details and just add sbt-scalariform to your local plugin definition.
If you've got a more complicated build with several projects and are __upgrading from 1.4.0 or older__, see the next section below.
Local plugins are defined in a `plugins.sbt` file in the `project/` folder of your project.

To add sbt-scalariform to your build using sbt 0.13.5, just add the below setting, paying attention to blank lines:

```
... // Other settings
resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")
```

This will add the task `scalariformFormat` in the scopes `compile` and `test` and additionally run this task automatically when compiling;
for more control see the section *Advanced configuration* below

Now you are ready to go. Either start sbt or, if it was already started, reload the current session by executing the 
`reload` command. If everything worked, you should have the new command `scalariformFormat` available as well automatic 
formatting on `compile` and `test:compile` activated.


After adding the sbt-scalariform plugin like this, if you want different formatting settings from the default behavior, read 
the advanced configuration section. 

If you are using an sbt 0.13.x version older than 0.13.5, you'll want to upgrade, or stick to using sbt-scalariform 1.4.0.

sbt 0.12.x and below is not supported for this fork, see `https://github.com/sbt/sbt-scalariform` for
older versions.

Upgrading from 1.4.0: Disabling Auto-plugin Auto Formatting behavior
---------------------------------------------
Sbt-Scalariform versions 1.5.0 and greater have switched to using AutoPlugins for automatic settings in build files.
By default, when including this project, all of your projects defined will now have sbt-scalariform settings mixed into
each project. This means on compilation, your code will be formatted. If you'd like to keep sbt-scalariform
available for manual formatting with a sbt `scalariformFormat` command, you can mix in `defaultScalariformSettings` to your project -- 
see the advanced configuration section for details on how to do this.

Autoplugins only work with sbt 0.13.5 or greater.  

If you'd like to disable this plugin entirely on a per project basis, you can use sbt's built in feature to disable auto plugins:

```
import com.typesafe.sbt.SbtScalariform

lazy val project = Project(
    ...
).disablePlugins(SbtScalariform)
```

Using sbt-scalariform
---------------------

If you added the settings for this plugin like described above, you can either format your sources manually or automatically:

- Whenever you run the tasks `compile` or `test:compile`, your source files will be automatically formatted by Scalariform

- If you want to start formatting your source files explicitly, just run the task `scalariformFormat` or `test:scalariformFormat`

Advanced configuration
----------------------
sbt-scalariform comes with various configuration options. Changing the formatting preferences and deactivating the automatic formatting on compile are probably the most important ones and described in detail.

You can provide your own formatting preferences for Scalariform via the setting key `ScalariformKeys.preferences` which expects an instance of `IFormattingPreferences`. Make sure you import all necessary members from the package `scalariform.formatter.preferences`. Let's look at an example:

.sbt build example
```
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform

SbtScalariform.scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(PreserveDanglingCloseParenthesis, true)
```

.scala build example
```
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform

lazy val project = Project(
    ...
    settings = SbtScalariform.scalariformSettings ++ Seq(
      ScalariformKeys.preferences := ScalariformKeys.preferences.value
        .setPreference(AlignSingleLineCaseStatements, true)
        .setPreference(DoubleIndentClassDeclaration, true)
        .setPreference(PreserveDanglingCloseParenthesis, true)
    )
)
```

If you don't want sbt to automatically format your source files when the tasks `compile` or `test:compile`, just add 
`defaultScalariformSettings` instead of `scalariformSettings` to your build definition.

.scala build example:
```
import com.typesafe.sbt.SbtScalariform

lazy val project = Project(
    "project_name",
    file("."),
    settings = SbtScalariform.defaultScalariformSettings ++ 
    ...
)
```

.sbt build example:

```
import com.typesafe.sbt.SbtScalariform

SbtScalariform.defaultScalariformSettings
```


If you want to additionally enable Scalariform for your integration tests, use `scalariformSettingsWithIt` or `defaultScalariformSettingsWithIt` instead of the above.

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
