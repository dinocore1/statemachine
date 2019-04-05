package com.devsmart.statemachine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Statemachine<S extends Enum<S>, E extends Enum<E>> {

    public interface EventListener<S extends Enum<S>, E extends Enum<E>> {
        void onStateChange(Statemachine<S, E> fsm, S exit, S enter, E event, Object data);
    }

    private static class Edge<S extends Enum<S>, E extends Enum<E>> implements Comparable<Edge<S, E>> {

        final S from;
        final E event;

        public Edge(S from, E event) {
            this.from = from;
            this.event = event;
        }

        @Override
        public int hashCode() {
            return from.hashCode() ^ event.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null || obj.getClass() != getClass()) {
                return false;
            }

            Edge other = (Edge) obj;
            return compareTo(other) == 0;
        }

        @Override
        public int compareTo(Edge<S, E> o) {
            int ret = from.compareTo(o.from);
            if(ret == 0) {
                ret = event.compareTo(o.event);
            }
            return ret;
        }

        @Override
        public String toString() {
            return String.format("%s ==%s==> ", from, event);
        }
    }

    private HashMap<Edge<S, E>, S> mGraph;
    private S mStartState;
    private S mState;
    private Set<EventListener<S, E>> mListeners = new HashSet<>();


    public static class Builder<S extends Enum<S>, E extends Enum<E>> {

        private final S mStartState;
        private HashMap<Edge<S, E>, S> mGraph = new HashMap<>();

        public Builder(S startState) {
            mStartState = startState;
        }

        public Builder<S, E> configure(S from, S to, E event) {
            Edge<S, E> edge = new Edge<>(from, event);
            S end = mGraph.get(edge);
            if(end != null) {
                throw new IllegalStateException(String.format("already contains %s %s", edge, to));
            } else {
                mGraph.put(edge, to);
            }


            return this;
        }

        public Statemachine<S, E> build() {
            Statemachine<S, E> retval = new Statemachine<>();
            retval.mStartState = mStartState;
            retval.mState = mStartState;
            retval.mGraph = mGraph;
            return retval;
        }

    }

    private Statemachine() {
    }


    public S getState() {
        return mState;
    }

    public void input(E event, Object obj) {
        Edge<S, E> e = new Edge<>(mState, event);
        S nextState = mGraph.get(e);
        if(nextState != null) {
            S oldState = mState;
            mState = nextState;
            dispatch(oldState, nextState, event, obj);
        }
    }

    public void input(E event) {
        input(event, null);
    }

    public void addListener(EventListener<S, E> listener) {
        mListeners.add(listener);
    }

    public void removeListener(EventListener<S, E> listener) {
        mListeners.remove(listener);
    }

    private void dispatch(S oldState, S nextState, E event, Object data) {
        for(EventListener<S, E> l : mListeners) {
            l.onStateChange(this, oldState, nextState, event, data);
        }
    }
}
