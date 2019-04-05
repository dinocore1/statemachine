package com.devsmart.statemachine;

public interface EventListener<S extends Enum<S>, E extends Enum<E>, C extends StateContext> {
    void onStateChange(C context, S exit, S enter, E event);
}
