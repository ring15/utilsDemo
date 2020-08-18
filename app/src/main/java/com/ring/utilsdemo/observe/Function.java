package com.ring.utilsdemo.observe;

/**
 * Created by ring on 2020/8/12.
 */
public interface Function<T, R> {

    R apply(T t) throws Exception;

}
