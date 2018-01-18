package xtend.net;

/**
 * Created by eliezer on 12/25/17.
 */

public interface Callback<T> {
    void apply(T data);
}