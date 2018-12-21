package net.cpollet.kozan.generator;

import java.util.Arrays;
import java.util.Iterator;

public final class LoopingGenerator<T> implements Generator<T> {
    private final Iterable<T> iterable;
    private Iterator<T> it;

    public LoopingGenerator(Iterable<T> iterable) {
        this.iterable = iterable;
        this.it = iterable.iterator();
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public LoopingGenerator(T... values) {
        this(Arrays.asList(values));
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public T next() {
        if (!it.hasNext()) {
            it = iterable.iterator();
        }
        return it.next();
    }
}
