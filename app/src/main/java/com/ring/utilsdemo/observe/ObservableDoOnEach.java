package com.ring.utilsdemo.observe;

/**
 * Created by ring on 2020/8/12.
 */
public class ObservableDoOnEach<T> extends Observable<T> {

    private final Consumer<T> onNext;
    private final ObservableSource<T> source;

    public ObservableDoOnEach(ObservableSource<T> source, Consumer<T> onNext) {
        this.onNext = onNext;
        this.source = source;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        source.subscribe(new DoOnEachObserver<>(onNext, observer));
    }

    static final class DoOnEachObserver<T> implements Observer<T> {

        private final Consumer<T> onNext;
        private final Observer<? super T> observer;

        public DoOnEachObserver(Consumer<T> onNext, Observer<? super T> observer) {
            this.onNext = onNext;
            this.observer = observer;
        }

        @Override
        public void onNext(T value) {
            try {
                onNext.accept(value);
            } catch (Exception e) {
                e.printStackTrace();
                observer.onError(e);
            }
            observer.onNext(value);
        }

        @Override
        public void onError(Exception error) {
            observer.onError(error);
        }
    }
}
