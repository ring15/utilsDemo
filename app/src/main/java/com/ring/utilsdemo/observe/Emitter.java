package com.ring.utilsdemo.observe;

/**
 * Created by ring on 2020/8/12.
 */
public interface Emitter<T> {

    void onNext(T value);

    void onError(Exception error);

}
