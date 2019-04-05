[![Build Status](https://travis-ci.com/dinocore1/statemachine.svg?branch=master)](https://travis-ci.com/dinocore1/statemachine)

### Statemachine for Java

A simple FSM library for Java


#### Configure Statemachine

```java
private enum State {
    Locked,
    Unlocked
}

private enum Event {
    Push,
    Coin
}
    
Statemachine<State, Event> fsm = new Statemachine.Builder<State, Event>(State.Locked)
                .configure(State.Locked, State.Unlocked, Event.Coin)
                .configure(State.Locked, State.Locked, Event.Push)
                .configure(State.Unlocked, State.Locked, Event.Push)
                .configure(State.Unlocked, State.Unlocked, Event.Coin)
                .build();

```

Then send events to the state machine

```java
fsm.input(Event.Push);
assertEquals(State.Locked, fsm.getState());

fsm.input(Event.Push);
assertEquals(State.Locked, fsm.getState());

fsm.input(Event.Coin);
assertEquals(State.Unlocked, fsm.getState());
```