package xtend.net;

/**
 * Created by eliezer on 12/25/17.
 */

public interface BodyMapper<T> {
    Object map(String data, Class<T> type);
}
