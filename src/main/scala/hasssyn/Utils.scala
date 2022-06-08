import cats.syntax.either._
import io.circe._, io.circe.parser._
import java.io.{File, PrintWriter}
import java.time._
import scala.io.Source

//TODO Refactoring
object Utils {
  // encoding
  private val ENC = "utf8"

  def readFile(filename: String): String =
    val source = Source.fromFile(filename, ENC)
    val str = source.mkString
    source.close
    str

  def mkdir(name: String): Unit = new File(name).mkdirs

  def getPrintWriter(filename: String): PrintWriter =
    new PrintWriter(new File(filename))

  def dumpFile(data: Any, filename: String): Unit =
    val nf = getPrintWriter(filename)
    nf.print(data)
    nf.close()

  def toJson(data: String): Json =
    parse(data).getOrElse(Json.Null)

  def toTime(str: String): Time = OffsetDateTime.parse(str).toEpochSecond

  def toState(str: String): Option[State] = strStateMap.get(str)

  def toEntity(str: String): Option[Entity] = strEntityMap.get(str)

  def json2Event(json: Json, prev: State): (Event, State) =
    val cursor: HCursor = json.hcursor
    val entity =
      val entityStr = cursor.downField("entity_id").as[String].toOption.get
      toEntity(entityStr).get
    val state =
      val stateStr = cursor.downField("state").as[String].toOption.get
      toState(stateStr).get
    val time =
      val timeStr = cursor.downField("last_changed").as[String].toOption.get
      toTime(timeStr)
    (Event(entity, (prev, state), time), state)

  def json2Input(json: Json): Input =
    var input: Input = List()
    val entityJsons = json.hcursor.values.get
    for {
      entityJson <- entityJsons
    } {
      var prev_state: State = InitState.Init
      val cursor = entityJson.hcursor
      val jsons = cursor.values.get
      for {
        json <- jsons
      } {
        val (event, state) = json2Event(json, prev_state)
        prev_state = state
        input :+= event
      }
    }

    input

  def json2IO(json: Json, action: Action): (Input, Output) =
    val input = json2Input(json)
    val times = input
      .filter(e => e.entity == action.entity && e.change == action.change)
      .map(e => e.time)
    val output = (action, times)
    (input, output)
}
