package net.cpollet.kozan.lazy;

import java.util.function.Supplier;

public final class Lazy<T> {
    private T value;
    private Supplier<T> supplier;

    public Lazy(Supplier<T> supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null");
        }

        this.supplier = supplier;
    }

    public Lazy(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        this.value = value;
    }

    public T value() {
        if (value == null) {
            value = supplier.get();

            if (value == null) {
                throw new IllegalStateException("Supplier must not return a null value");
            }
        }

        return value;
    }

    public boolean hasValue() {
        return value != null;
    }
}
