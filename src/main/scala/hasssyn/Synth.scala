/** Trigger */
val sunTriggerPool: LazyList[Trigger] =
  val sunStates = SunState.values
  LazyList.from(for {
    w1 <- sunStates
    w2 <- sunStates
    if w1 != w2
  } yield SunTrigger((w1, w2)))

val userTriggerPool: LazyList[Trigger] =
  val userStates = UserState.values
  LazyList.from(for {
    w1 <- userStates
    w2 <- userStates
    if w1 != w2
  } yield UserTrigger((w1, w2)))

val fanTriggerPool: LazyList[Trigger] =
  val fanStates = FanState.values
  LazyList.from(for {
    w1 <- fanStates
    w2 <- fanStates
    if w1 != w2
  } yield FanTrigger((w1, w2)))

val weatherTriggerPool: LazyList[Trigger] =
  val weatherStates = WeatherState.values
  LazyList.from(for {
    w1 <- weatherStates
    w2 <- weatherStates
    if w1 != w2
  } yield WeatherTrigger((w1, w2)))

val triggerPool: LazyList[Trigger] =
  sunTriggerPool #::: userTriggerPool #::: fanTriggerPool #::: weatherTriggerPool

/** Condition */
val sunConditionPool: LazyList[Option[Condition]] =
  val sunStates = SunState.values
  None +: LazyList.from(for {
    w1 <- sunStates
  } yield Some(SunCondition(w1)))

val userConditionPool: LazyList[Option[Condition]] =
  val userStates = UserState.values
  None +: LazyList.from(for {
    w1 <- userStates
  } yield Some(UserCondition(w1)))

val fanConditionPool: LazyList[Option[Condition]] =
  val fanStates = FanState.values
  None +: LazyList.from(for {
    w1 <- fanStates
  } yield Some(FanCondition(w1)))

val weatherConditionPool: LazyList[Option[Condition]] =
  val weatherStates = WeatherState.values
  None +: LazyList.from(for {
    w1 <- weatherStates
  } yield Some(WeatherCondition(w1)))

val conditionsPool: LazyList[List[Condition]] =
  (for {
    sOpt <- sunConditionPool
    uOpt <- userConditionPool
    fOpt <- fanConditionPool
    wOpt <- weatherConditionPool
  } yield List(sOpt, uOpt, fOpt, wOpt).flatten)

/** Action */
val fanActionPool: LazyList[Action] =
  val fanStates = FanState.values
  LazyList.from(for {
    w1 <- fanStates
    w2 <- fanStates
    if w1 != w2
  } yield FanAction((w1, w2)))

val actionPool: LazyList[Action] = fanActionPool

val automationPool =
  for {
    t <- triggerPool
    cs <- conditionsPool
    a <- actionPool
  } yield Automation(t, cs, a)

def synthesize(input: Input, output: Output, error: Long) =
  val action = output._1
  automationPool
    .filter(auto =>
      auto.trigger.entity != action.entity && auto.action == action && auto
        .interp(input, output, error),
    )
    // .toList
    .headOption match {
    case Some(auto) => auto
    case None       => throw new Exception("Synthesis fail")
  }
