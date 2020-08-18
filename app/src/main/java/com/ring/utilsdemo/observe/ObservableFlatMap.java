package com.ring.utilsdemo.observe;

/**
 * Created by ring on 2020/8/12.
 */
public class ObservableFlatMap<T, U> extends Observable<U> {

    private final Function<T, ? extends ObservableSource<U>> mapper;
    private final ObservableSource<T> source;

    public ObservableFlatMap(ObservableSource<T> source, Function<T, ? extends ObservableSource<U>> mapper) {
        this.mapper = mapper;
        this.source = source;
    }

    @Override
    protected void subscribeActual(Observer<? super U> observer) {
        source.subscribe(new MergeObserver<>(observer, mapper));
    }

    static final class MergeObserver<T, U> implements Observer<T> {

        final Function<T, ? extends ObservableSource<U>> mapper;
        final Observer<? super U> source;

        public MergeObserver(Observer<? super U> source, Function<T, ? extends ObservableSource<U>> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public void onNext(T value) {
            try {
                ObservableSource<U> observableSource = mapper.apply(value);
                onNextInner(observableSource);
            } catch (Exception e) {
                e.printStackTrace();
                onError(e);
            }
        }

        private void onNextInner(ObservableSource<U> observableSource) {
            observableSource.subscribe(new InnerObserver<>(this));
        }

        @Override
        public void onError(Exception error) {
            source.onError(error);
        }

        void tryEmit(U value, InnerObserver<T, U> tuInnerObserver) {
            source.onNext(value);
        }
    }

    static final class InnerObserver<T, U> implements Observer<U> {

        final MergeObserver<T, U> parent;

        InnerObserver(MergeObserver<T, U> parent) {
            this.parent = parent;
        }

        @Override
        public void onNext(U value) {
            parent.tryEmit(value, this);
        }

        @Override
        public void onError(Exception error) {
            parent.onError(error);
        }
    }
}
