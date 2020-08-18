package com.ring.utilsdemo.observe;

/**
 * Created by ring on 2020/8/12.
 */
public interface ObservableSource<T> {
    void subscribe(Observer<? super T> observer);
}
