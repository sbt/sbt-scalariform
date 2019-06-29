sbt-scalariform
===============

Welcome to sbt-scalariform, an <a href="https://github.com/sbt/sbt">sbt</a> plugin adding support for source code formatting
using <a href="https://github.com/scala-ide/scalariform">Scalariform</a>.

Installing
--------------------------

```
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.3")
```

Configuration (build.sbt)
----------------------

Imports
```
import scalariform.formatter.preferences._
```

Example Preferences:

```
scalariformPreferences := scalariformPreferences.value
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentConstructorArguments, true)
    .setPreference(DanglingCloseParenthesis, Preserve)
```

Sources are automatically formatted on `compile` and `test:compile` by default.

To enable Scalariform for integration tests in addition to `compile` and `test:compile` add to the build:

```
scalariformItSettings
```

Other useful configuration options are provided by sbt setting keys:

- `includeFilter in scalariformFormat`: Defaults to **.scala*
- `excludeFilter in scalariformFormat`: Using the default of sbt

Configuration (filesystem)
----------------------

Copy [default Scalariform preferences](https://github.com/scala-ide/scalariform/blob/master/formatterPreferences.properties)
and create a preferences file globally in `~/.scalariform.conf`, or locally in `projectRoot/.scalariform.conf`.

Modify preferences accordingly.

Note: It is *not* recommended to mix build and filesystem level preferences. If for some reason this is required,
to override, for example, global filesystem preferences, create an empty `.scalariform.conf` file in the project root
and define build level preferences accordingly.


Disable Autoformatting / Enable Formatting of Base Directory Sources
----------------------

Custom configuration options can be applied in the build, or in a `.scalariform.conf` preferences file.

Build

```
scalariformAutoformat := false
scalariformWithBaseDirectory := true
```

Filesystem

add to the top of target `.scalariform.conf` file:
```
autoformat=false
withBaseDirectory=true
```

License
-------

This code is open source software licensed under the <a href="http://www.apache.org/licenses/LICENSE-2.0.html">Apache 2.0 License</a>.
