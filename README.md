sbt-scalariform
===============

Welcome to sbt-scalariform, an <a href="https://github.com/sbt/sbt">sbt</a> plugin adding support for source code formatting
using <a href="https://github.com/scala-ide/scalariform">Scalariform</a>.

Installing
--------------------------

```
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.0")
```

Configuration (build.sbt)
----------------------

Imports
```
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._
```

Example Preferences
```
val preferences =
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentConstructorArguments, true)
    .setPreference(DanglingCloseParenthesis, Preserve)
```

Sources are automatically formatted on `compile` and `test:compile` by default, just add formatting preferences to the build:
```
Seq(preferences)
```

To enable Scalariform for integration tests in addtion to `compile` and `test:compile` add to the build:
```
scalariformSettingsWithIt(autoformat = true)
Seq(preferences)
```

Other useful configuration options are provided by sbt setting keys:

- `includeFilter in scalariformFormat`: Defaults to **.scala*
- `excludeFilter in scalariformFormat`: Using the default of sbt


Disable Autoformatting
----------------------

There are two ways to disable autoformatting: in the build, or in a filesystem preferences file.

Build
```
scalariformSettings(autoformat = false)
Seq(preferences)
```

Filesystem (globally, `~/.scalariform.conf`, or in `projectRoot/.scalariform.conf`)

Copy [default Scalariform preferences](https://github.com/scala-ide/scalariform/blob/master/formatterPreferences.properties) and add
to the top of the `scalariform.conf` file:

```
autoformat=false
```

It is *not* recommended to mix build and filesystem level preferences. If for some reason this is required,
to override, for example, global filesystem preferences, create an empty `.scalariform.conf` file in the project root and
define build level preferences accordingly.


License
-------

This code is open source software licensed under the <a href="http://www.apache.org/licenses/LICENSE-2.0.html">Apache 2.0 License</a>.
