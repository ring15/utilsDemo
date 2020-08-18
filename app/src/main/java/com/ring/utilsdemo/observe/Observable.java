package com.ring.utilsdemo.observe;


/**
 * Created by ring on 2020/8/12.
 */
public abstract class Observable<T> implements ObservableSource<T> {

    public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
        return new ObservableCreate<T>(source);
    }

    public <U> Observable<U> flatMap(Function<T, ? extends ObservableSource<U>> mapper) {
        return new ObservableFlatMap<>(this, mapper);
    }

    public <U> Observable<U> map(Function<T, U> mapper) {
        return new ObservableMap<>(this, mapper);
    }

    public Observable<T> doOnNext(Consumer<T> consumer) {
        return new ObservableDoOnEach<>(this, consumer);
    }

    @Override
    public void subscribe(Observer<? super T> observer) {
        subscribeActual(observer);
    }

    protected abstract void subscribeActual(Observer<? super T> observer);

}
