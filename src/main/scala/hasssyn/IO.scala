type Time = Long

/** Input */
case class Event(entity: Entity, change: Change, time: Time)

type Input = List[Event]

/** Output */
type Output = (Action, List[Time])

/** Value */
case class Value(times: List[Time])

extension (value: Value) {
  def isContained(output: Output, error: Long): Boolean = {
    val times = value.times
    times != Nil &&
    times.forall(vt =>
      output._2.exists(ot => (0 <= (ot - vt) && (ot - vt) < error)),
    )
  }
}
