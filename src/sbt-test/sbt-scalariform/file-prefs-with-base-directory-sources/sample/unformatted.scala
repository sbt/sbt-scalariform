object Foo {
  def method(
    string: String,
    int: Int) = {
    string match {
      case "wibble" => 42
      case "foo" => 123
      case _ => 100
    }
  }
  method(
    string = "hello",
    int = 1)
}
