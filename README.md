sbt-scalariform
===============

Welcome to sbt-scalariform, an <a href="https://github.com/sbt/sbt">sbt</a> plugin adding support for source code formatting
using <a href="https://github.com/scala-ide/scalariform">Scalariform</a>.

Installing
--------------------------

```
// project/plugins.sbt
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.7.0")
```

Configuration (build.sbt)
----------------------

Imports
```
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
```

Example Preferences
```
val preferences =
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentConstructorArguments, true)
    .setPreference(DanglingCloseParenthesis, Preserve)
```

Automatically format on `compile` or `test:compile`
```
SbtScalariform.scalariformSettings ++ Seq(preferences)
```

Format on demand (run `scalariformFormat` or `test:scalariformFormat` to format files)
```
SbtScalariform.formatOnDemandSettings ++ Seq(preferences)
```

If you want to additionally enable Scalariform for your integration tests, use `scalariformSettingsWithIt` or `defaultScalariformSettingsWithIt` instead of the above.

Other useful configuration options are provided by common sbt setting keys:

- `includeFilter in scalariformFormat`: Defaults to **.scala*
- `excludeFilter in scalariformFormat`: Using the default of sbt

Disabling
--------------------------

```
import com.typesafe.sbt.SbtScalariform

lazy val project = Project(...).disablePlugins(SbtScalariform)
```

License
-------

This code is open source software licensed under the <a href="http://www.apache.org/licenses/LICENSE-2.0.html">Apache 2.0 License</a>.
