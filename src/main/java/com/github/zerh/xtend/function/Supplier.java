package com.github.zerh.xtend.function;

@FunctionalInterface
public interface Supplier<T> {
    T get();
}