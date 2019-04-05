package com.devsmart.statemachine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Statemachine<S extends Enum<S>, E extends Enum<E>, C extends StateContext> {

    private static class Node<S extends Enum<S>> implements Comparable<Node<S>> {

        final S state;

        Runnable onEnter;
        Runnable onExit;

        public Node(S state) {
            this.state = state;
        }

        @Override
        public int hashCode() {
            return state.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null || obj.getClass() != this.getClass()) {
                return false;
            }

            Node other = (Node) obj;
            return compareTo(other) == 0;
        }

        @Override
        public int compareTo(Node<S> o) {
            return state.compareTo(o.state);
        }

        @Override
        public String toString() {
            return state.toString();
        }
    }

    private static class Edge<S extends Enum<S>, E extends Enum<E>> implements Comparable<Edge<S, E>> {

        final Node<S> to;
        final E event;

        public Edge(Node<S> to, E event) {
            this.to = to;
            this.event = event;
        }

        @Override
        public int hashCode() {
            return to.hashCode() ^ event.hashCode();
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
            int ret = to.compareTo(o.to);
            if(ret == 0) {
                ret = event.compareTo(o.event);
            }
            return ret;
        }

        @Override
        public String toString() {
            return String.format("== %s ==> %s", event, to);
        }
    }

    private C mContext;
    private HashMap<Node, Set<Edge>> mGraph;
    private S mStartState;
    private S mState;


    public static class Builder<S1 extends Enum<S1>, E1 extends Enum<E1>, C1 extends StateContext> {

        private final S1 mStartState;
        private HashMap<Node, Set<Edge>> mGraph = new HashMap<>();

        public Builder(S1 startState) {
            mStartState = startState;
        }

        public Builder<S1, E1, C1> configure(S1 from, S1 to, E1 event) {
            Node<S1> fromNode = new Node<>(from);
            Node<S1> toNode = new Node<>(to);
            Edge<S1, E1> edge = new Edge<>(toNode, event);
            Set<Edge> edges = mGraph.get(fromNode);
            if(edges == null) {
                edges = new HashSet<>();
                mGraph.put(fromNode, edges);
            }

            if(!edges.add(edge)) {
                throw new IllegalStateException(String.format("already contains %s %s", fromNode, edge));
            }

            return this;
        }

        public Statemachine<S1, E1, C1> build() {
            Statemachine<S1, E1, C1> retval = new Statemachine<>();
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
        Set<Edge> edges = mGraph.get(event);
        if(edges != null) {
            
        }

    }
}
