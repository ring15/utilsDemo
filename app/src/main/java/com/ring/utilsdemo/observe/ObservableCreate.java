package com.ring.utilsdemo.observe;


/**
 * Created by ring on 2020/8/12.
 */
public class ObservableCreate<T> extends Observable<T> {

    final ObservableOnSubscribe<T> source;

    public ObservableCreate(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        CreateEmitter<T> parent = new CreateEmitter<>(observer);
        try {
            source.subscribe(parent);
        } catch (Exception e) {
            e.printStackTrace();
            observer.onError(e);
        }
    }

    static class CreateEmitter<T> implements Emitter<T> {

        final Observer<? super T> mObserver;

        public CreateEmitter(Observer<? super T> observer) {
            mObserver = observer;
        }

        @Override
        public void onNext(T value) {
            mObserver.onNext(value);
        }

        @Override
        public void onError(Exception error) {
            mObserver.onError(error);
        }
    }
}
