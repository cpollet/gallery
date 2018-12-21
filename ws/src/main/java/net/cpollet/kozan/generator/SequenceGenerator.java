package net.cpollet.kozan.generator;

import java.util.function.Function;

public final class SequenceGenerator<T> implements Generator<T> {
    private final Function<T, T> generator;
    private T previous;

    public SequenceGenerator(T seed, Function<T, T> generator) {
        this.generator = generator;
        this.previous = seed;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @SuppressWarnings("squid:S2272") // infinite sequence always has a next() element
    @Override
    public T next() {
        T retVal = previous;
        previous = generator.apply(previous);
        return retVal;
    }
}
