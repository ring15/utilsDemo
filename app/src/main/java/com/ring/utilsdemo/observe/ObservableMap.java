package com.ring.utilsdemo.observe;

/**
 * Created by ring on 2020/8/12.
 */
public class ObservableMap<T, U> extends Observable<U> {

    private final Function<T, U> function;
    private final ObservableSource<T> source;

    public ObservableMap(ObservableSource<T> source, Function<T, U> function) {
        this.function = function;
        this.source = source;
    }

    @Override
    protected void subscribeActual(Observer<? super U> observer) {
        source.subscribe(new MapObservable<>(function, observer));

    }

    static final class MapObservable<T, U> implements Observer<T> {

        final Function<T, U> mapper;
        final Observer<? super U> source;

        public MapObservable(Function<T, U> mapper, Observer<? super U> source) {
            this.mapper = mapper;
            this.source = source;
        }

        @Override
        public void onNext(T value) {
            try {
                U u = mapper.apply(value);
                source.onNext(u);
            } catch (Exception e) {
                e.printStackTrace();
                onError(e);
            }
        }

        @Override
        public void onError(Exception error) {
            source.onError(error);
        }
    }
}
