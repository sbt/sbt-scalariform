import sbt._

object Version {
  val scalariform = "0.1.5"
}

object Library {
  val scalariform = "com.danieltrinh" %% "scalariform" % Version.scalariform
}

object Dependencies {

  import Library._

  val sbtScalariform = List(
    scalariform
  )
}