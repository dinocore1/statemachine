package com.devsmart.statemachine;

import java.util.HashMap;

public class Statemachine<S extends Enum<S>, E extends Enum<E>, C extends StateContext> {


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

    private C mContext;
    private HashMap<Edge<S, E>, S> mGraph;
    private S mStartState;
    private S mState;


    public static class Builder<S extends Enum<S>, E extends Enum<E>, C1 extends StateContext> {

        private final S mStartState;
        private HashMap<Edge<S, E>, S> mGraph = new HashMap<>();

        public Builder(S startState) {
            mStartState = startState;
        }

        public Builder<S, E, C1> configure(S from, S to, E event) {
            Edge<S, E> edge = new Edge<>(from, event);
            S end = mGraph.get(edge);
            if(end != null) {
                throw new IllegalStateException(String.format("already contains %s %s", edge, to));
            } else {
                mGraph.put(edge, to);
            }


            return this;
        }

        public Statemachine<S, E, C1> build() {
            Statemachine<S, E, C1> retval = new Statemachine<>();
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

    public <C1 extends C> void input(E event, C1 context) {
        Edge<S, E> e = new Edge<>(mState, event);
        S nextState = mGraph.get(e);
        if(nextState != null) {
            S oldState = mState;
            mState = nextState;
            dispatch(context, oldState, nextState, event);
        }
    }

    public void input(E event) {
        input(event, null);
    }

    private <C1 extends C> void dispatch(C1 context, S oldState, S nextState, E event) {

    }
}
