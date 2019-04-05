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

    @Test(expected = IllegalStateException.class)
    public void testBuildAlreadyContainsEdge() {

        Statemachine<State, Event, StateContext> sm = new Statemachine.Builder()
                .configure(State.Locked, State.Unlocked, Event.Coin)
                .configure(State.Locked, State.Unlocked, Event.Coin)
                .build();




    }
}