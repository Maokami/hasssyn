case class Automation(
  trigger: Trigger,
  conditions: List[Condition],
  action: Action,
)

extension (auto: Automation) {

  /** Currently, the conversion of automation to YAML is hand-written. This can
    * be done by circe enc/decoder
    */
  def toYaml: String =
    val triggerYAML = auto.trigger.toYaml
    val conditionYAML = auto.conditions.foldLeft("")((s, c) => s + c.toYaml)
    val actionYAML = auto.action.toYaml
    s"""|- id: 'Change this to arbitary string'
    |  description: ''
    |  trigger:$triggerYAML
    |  condition:$conditionYAML
    |  action:$actionYAML
    |  mode: single""".stripMargin

  def dumpYaml(filename: String) =
    val data = auto.toYaml
    Utils.dumpFile(data, filename)
}
class Trigger(val entity: Entity, val change: Change)
case class SunTrigger(override val change: (SunState, SunState))
  extends Trigger(Entity.Sun, change)
case class UserTrigger(override val change: (UserState, UserState))
  extends Trigger(Entity.User, change)
case class FanTrigger(override val change: (FanState, FanState))
  extends Trigger(Entity.Fan, change)
case class WeatherTrigger(override val change: (WeatherState, WeatherState))
  extends Trigger(Entity.Weather, change)
extension (trigger: Trigger) {
  def toYaml: String =
    val id = trigger.entity.toStr
    val from = trigger.change._1.toString
    val to = trigger.change._2.toString
    s"""
    |  - platform: state
    |    entity_id: $id
    |    from: $from
    |    to: $to""".stripMargin
}
class Condition(val entity: Entity, val state: State)
case class SunCondition(override val state: SunState)
  extends Condition(Entity.Sun, state)
case class UserCondition(override val state: UserState)
  extends Condition(Entity.User, state)
case class FanCondition(override val state: FanState)
  extends Condition(Entity.Fan, state)
case class WeatherCondition(override val state: WeatherState)
  extends Condition(Entity.Weather, state)
extension (condition: Condition) {
  def toYaml: String =
    val id = condition.entity.toStr
    val state = condition.state.toString
    s"""
    |  - condition: state
    |    entity_id: $id
    |    state: $state""".stripMargin
}

class Action(val entity: Entity, val change: Change)
case class FanAction(override val change: (FanState, FanState))
  extends Action(Entity.Fan, change)
//TODO : Refactoring
extension (action: Action) {
  def toYaml: String =
    val id = action.entity.toStr
    val service = action.change match {
      case (FanState.Off, FanState.On) => "fan.turn_on"
      case (FanState.On, FanState.Off) => "fan.turn_off"
      case _                           => ???
    }
    s"""
    |  - service: $service
    |    target:
    |      entity_id: $id""".stripMargin
}
