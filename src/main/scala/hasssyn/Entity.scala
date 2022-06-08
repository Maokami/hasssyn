import scala.collection.mutable.{Map => MMap}

enum Entity(id: String):
  val entity = id
  case Sun extends Entity("sun.sun")
  case User extends Entity("person.jaeho")
  case Fan extends Entity("fan.xiaomi_smart_fan")
  case Weather extends Entity("weather.weather")

val entityStrMap: Map[Entity, String] =
  Entity.values.map(e => (e, e.entity)).toMap

val strEntityMap: Map[String, Entity] =
  var map = MMap[String, Entity]()
  for {
    (entity, str) <- entityStrMap
  } {
    map += (str, entity)
  }
  map.toMap

extension (entity: Entity) {
  def toStr: String = entity.entity
}

type State = InitState | SunState | UserState | FanState | WeatherState

enum InitState:
  case Init

enum SunState:
  case Below, Above

enum UserState:
  case Home, NotHome

enum FanState:
  case On, Off

enum WeatherState:
  case Sunny, Cloudy, PartlyCloudy, Rainy

val strStateMap: Map[String, State] =
  Map(
    "below_horizon" -> SunState.Below,
    "above_horizon" -> SunState.Above,
    "home" -> UserState.Home,
    "not_home" -> UserState.NotHome,
    "on" -> FanState.On,
    "off" -> FanState.Off,
    "sunny" -> WeatherState.Sunny,
    "cloudy" -> WeatherState.Cloudy,
    "partlycloudy" -> WeatherState.PartlyCloudy,
    "rainy" -> WeatherState.Rainy,
  )

val stateStrMap: Map[State, String] =
  var map = MMap[State, String]()
  for {
    (str, state) <- strStateMap
  } {
    map += (state, str)
  }
  map.toMap

extension (state: State) {
  def toStr: Option[String] = stateStrMap.get(state)
}

type Change = (State, State)
