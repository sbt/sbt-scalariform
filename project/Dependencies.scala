import sbt._

object Version {
  val scalariform = "0.1.8"
}

object Library {
  val scalariform = "org.scalariform" %% "scalariform" % Version.scalariform
}

object Dependencies {

  import Library._

  val sbtScalariform = List(
    scalariform
  )
}