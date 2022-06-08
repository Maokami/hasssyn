@main def hassSyn(
  input_file: String,
  output_file: String,
  action: String,
  error: Long,
) = {
  val currentDir = new java.io.File(".").getCanonicalPath
  val input_path = s"$currentDir/$input_file.json"
  val output_path = s"$currentDir/$output_file.yaml"

  val data = Utils.readFile(input_path)
  val json = Utils.toJson(data)
  val targetAction = action match {
    case "turnOff" => FanAction((FanState.On, FanState.Off))
    case "turnOn"  => FanAction((FanState.Off, FanState.On))
    case _ =>
      throw new Exception(
        "Currently, only 'turnOn' and 'turnOff' actions are supported.",
      )
  }
  val (input, output) = Utils.json2IO(json, targetAction)
  val auto = synthesize(input, output, error)
  auto.dumpYaml(output_path)
  println("hassSyn success")
}
