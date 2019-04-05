package com.devsmart.statemachine;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
        Statemachine<State, Event> sm = new Statemachine.Builder<State, Event>(State.Locked)
                .configure(State.Locked, State.Unlocked, Event.Coin)
                .configure(State.Locked, State.Locked, Event.Push)
                .configure(State.Unlocked, State.Locked, Event.Push)
                .configure(State.Unlocked, State.Unlocked, Event.Coin)
                .build();

        assertEquals(State.Locked, sm.getState());
        sm.input(Event.Push);
        assertEquals(State.Locked, sm.getState());
        sm.input(Event.Coin);
        assertEquals(State.Unlocked, sm.getState());
        sm.input(Event.Push);
        assertEquals(State.Locked, sm.getState());

    }

    @Test(expected = IllegalStateException.class)
    public void testBuildAlreadyContainsEdge() {

        Statemachine<State, Event> sm = new Statemachine.Builder<State, Event>(State.Locked)
                .configure(State.Locked, State.Unlocked, Event.Coin)
                .configure(State.Locked, State.Unlocked, Event.Coin)
                .build();


    }

    @Test
    public void testListener() {
        Statemachine<State, Event> sm = new Statemachine.Builder<State, Event>(State.Locked)
                .configure(State.Locked, State.Unlocked, Event.Coin)
                .configure(State.Locked, State.Locked, Event.Push)
                .configure(State.Unlocked, State.Locked, Event.Push)
                .configure(State.Unlocked, State.Unlocked, Event.Coin)
                .build();

        Statemachine.EventListener callback = mock(Statemachine.EventListener.class);
        sm.addListener(callback);

        sm.input(Event.Coin);

        verify(callback, times(1)).onStateChange(sm, State.Locked, State.Unlocked, Event.Coin, null);

    }
}
