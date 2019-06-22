package com.github.zerh.xtend;

import com.github.zerh.xtend.function.Function;

class Persistent<R> {

    private R value;
    private boolean done = true;
    private int id;

    Persistent(int id) {
        this.id = id;
    }

    Persistent<R> tryGet(Function<Integer, R> function) {
        try {
            value = function.apply(id);
            done = true;
        } catch (Exception ex) {
            done = false;
        }
        return this;
    }

    Persistent<R> ifCatch(Function<Integer, R> function) {
        if (!done) tryGet(function);
        return this;
    }

    R orElse(R orElse) {
        if (value == null)
            return orElse;
        return value;
    }

    R get() {
        return value;
    }

}