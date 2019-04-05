package com.devsmart.statemachine;

import org.junit.Test;

public class StatemachineTest {


    private enum State {
        Locked,
        Unlocked
    }

    private enum Event {
        Push,
        Coin
    }

    @Test
    public void testBuildSimple() {
        Statemachine<State, Event, StateContext> sm = new Statemachine.Builder<State, Event, StateContext>(State.Locked)
                .configure(State.Locked, State.Unlocked, Event.Coin)
                .configure(State.Locked, State.Locked, Event.Push)
                .configure(State.Unlocked, State.Locked, Event.Push)
                .configure(State.Unlocked, State.Unlocked, Event.Coin)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildAlreadyContainsEdge() {

        Statemachine<State, Event, StateContext> sm = new Statemachine.Builder<State, Event, StateContext>(State.Locked)
                .configure(State.Locked, State.Unlocked, Event.Coin)
                .configure(State.Locked, State.Unlocked, Event.Coin)
                .build();




    }
}
