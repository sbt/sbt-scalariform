sys.props.get("sbt-assembly.version") match {
  case Some(x) => addSbtPlugin("com.eed3si9n" % "sbt-assembly" % x)
  case _ => sys.error(
    "'sbt-assembly.version' property not defined! (add it to scriptedLaunchOpts -D)"
  )
}

sys.props.get("sbt-scalariform.version") match {
  case Some(x) => addSbtPlugin("org.scalariform" % "sbt-scalariform" % x)
  case _ => sys.error(
    "'sbt-scalariform.version' property not defined! (add it to scriptedLaunchOpts -D)"
  )
}
