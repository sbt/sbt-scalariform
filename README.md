sbt-scalariform
===============

Welcome to sbt-scalariform, an <a href="https://github.com/harrah/xsbt">sbt</a> plugin adding support for source code formatting using <a href="https://github.com/mdr/scalariform">Scalariform</a>.

Installing sbt-scalariform
--------------------------

sbt-scalariform is a plugin for sbt 0.12.0 or higher. Please make sure that you are using an appropriate sbt release. In order to download and install sbt, please refer to the <a href="http://github.com/harrah/xsbt/wiki/Getting-Started-Setup">sbt Getting Started Guide / Setup</a>.

As sbt-scalariform is a plugin for sbt, it is installed like any other sbt plugin, that is by mere configuration. For details about using sbt plugins, please refer to the <a href="http://github.com/harrah/xsbt/wiki/Getting-Started-Using-Plugins">sbt Getting Started Guide / Using Plugins</a>. 

Most probably you can skip the details and just add sbt-scalariform to your global or local plugin definition. Global plugins are defined in a `plugins.sbt` file in the `~/.sbt/plugins/` directory and local plugins are defined in a `plugins.sbt` file in the `project/` folder of your project. 

In order to add sbt-scalariform, just add the below setting to the relevant plugin definition, paying attention to blank lines between (existing) settings

```
... // Other settings

addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.0.0-SNAPSHOT")
```

After adding the sbt-scalariform plugin like this, you should either start sbt or, if it was already started, reload the current session by executing the `reload` command. If everything worked, you should have the new command `scalariform-format` available.

Basic configuration
-------------------

- If you installed this plugin globally (see above section), then you should add the `scalariformSettings` to your global build definition file `build.sbt` in the `~/.sbt/` directory

- If you installed this plugin locally (see above section) or if you prefer to have more flexibility to tweak your settings on a per project basis, then you should add the `scalariformSettings` to your local build definition file `build.sbt` or `project/Build.scala` of your project

- For `build.sbt`, paying attention to blank lines between (existing) settings:
  ```
  ... // Other settings
  
  scalariformSettings
  ```

- For `Build.scala`:
  ```
  lazy val myProject = Project(
    "myproject",
    file("."),
    settings = 
      ... /* other settings */ ++
      scalariformSettings
  )
  ```

- This will add the task `scalariform-format` in the scopes `compile` and `test` and additionally run this task automatically when compiling; for more control see the section *Advanced configuration* below

Using sbt-scalariform
---------------------

If you added the settings for this plugin like described above, you can either format your sources manually or automatically:

- Whenever you run the tasks `compile` or `test:compile`, your source files will be automatically formatted by Scalariform

- If you want to start formatting your source files explicitly, just run the task `scalariform-format` or `test:scalariform-format`

Advanced configuration
----------------------

sbt-scalariform comes with varoius configuration options. Changing the formatting preferences and deactivating the automatic formatting on compile are probably the most important ones and described in detail.

You can provide your own formatting preferences for Scalariform via the setting key `ScalariformKeys.preferences` which expects an instance of `IFormattingPreferences`. Make sure you import all necessary members from the package `scalariform.formatter.preferences`. Let's look at an example which would change the behavior of the default preferences provided by this plugin (by default the below preferences are set to `true`)

```
import scalariform.formatter.preferences._

ScalariformKeys.preferences := FormattingPreferences()
  .setPreference(DoubleIndentClassDeclaration, false).
  .setPreference(PreserveDanglingCloseParenthesis, false)
```

If you don't want sbt to automatically format your source files when the tasks `compile` or `test:compile`, just add `defaultScalariformSettings` instead of `scalariformSettings` to your build definition.

Other useful configuration options are provided by common sbt setting keys:

- `includeFilter in format`: Defaults to **.scala*
- `excludeFilter in format`: Using the default of sbt

Mailing list
------------

Please use the <a href="mailto:simple-build-tool@googlegroups.com">sbt mailing list</a> and prefix the subject with *[sbt-scalariform]*.

Contribution policy
-------------------

Contributions via GitHub pull requests are gladly accepted from their original author. Before we can accept pull requests, you will need to agree to the <a href="http://www.typesafe.com/contribute/cla">Typesafe Contributor License Agreement</a> online, using your GitHub account.

License
-------

This code is open source software licensed under the <a href="http://www.apache.org/licenses/LICENSE-2.0.html">Apache 2.0 License</a>. Feel free to use it accordingly.
