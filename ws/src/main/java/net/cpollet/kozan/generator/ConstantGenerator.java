package net.cpollet.kozan.generator;

public final class ConstantGenerator<T> implements Generator<T> {
    private final T value;

    public ConstantGenerator(T value) {
        this.value = value;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    @SuppressWarnings("squid:S2272") // we don't have a "last value"
    public T next() {
        return value;
    }
}
