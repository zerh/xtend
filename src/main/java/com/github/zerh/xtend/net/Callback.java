package com.github.zerh.xtend.net;

/**
 * Created by eliezer on 12/25/17.
 */

public interface Callback<T> {
    void accept(T data);
}
