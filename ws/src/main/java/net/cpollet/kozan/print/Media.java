package net.cpollet.kozan.print;

import java.util.function.Supplier;

public interface Media<T> {
    Media<T> with(String key, Supplier<Object> valueSupplier);

    T output();
}
