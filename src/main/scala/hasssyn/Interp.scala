extension (trigger: Trigger) {
  def interpT(input: Input): Value = {
    val change = trigger.change
    val times =
      input.filter(e => e.change == trigger.change).map(e => e.time)
    Value(times)
  }
}

extension (condition: Condition) {
  def interpC(input: Input, triggerV: Value): Value = {
    val entity = condition.entity
    val conditionInput = input.filter(e => e.entity == entity)
    val triggerTimes = triggerV.times
    def getState(time: Time) = {
      conditionInput.find(e => e.time > time) match {
        case Some(event) => event.change._1
        case None =>
          conditionInput.lastOption match {
            case None     => InitState
            case Some(le) => le.change._2
          }
      }
    }
    val times = triggerTimes.filter(tt => getState(tt) == condition.state)
    Value(times)
  }
}

extension (auto: Automation) {
  def interp(input: Input, output: Output, error: Long): Boolean =
    val triggerV = auto.trigger.interpT(input)
    val value =
      auto.conditions.foldLeft(triggerV)((v, cond) => cond.interpC(input, v))
    value.isContained(output, error)
}
