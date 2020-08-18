package com.ring.utilsdemo.observe;

/**
 * Created by ring on 2020/8/12.
 */
public interface ObservableOnSubscribe<T> {

    void subscribe(Emitter<T> emitter) throws Exception;

}
