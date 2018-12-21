package net.cpollet.kozan.generator;

import java.util.function.Supplier;

public final class SupplierGenerator<T> implements Generator<T> {
    private final Supplier<T> supplier;

    public SupplierGenerator(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @SuppressWarnings("squid:S2272") // infinite sequence always has a next() element
    @Override
    public T next() {
        return supplier.get();
    }
}
