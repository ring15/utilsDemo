package com.ring.utilsdemo.observe;

/**
 * Created by ring on 2020/8/12.
 */
public interface Consumer<T> {

    void accept(T t) throws Exception;

}
