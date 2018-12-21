package net.cpollet.kozan.collections;

import java.util.Collections;
import java.util.List;

public final class HeadAndTail<E> {
    @SuppressWarnings("unchecked")
    private static final HeadAndTail EMPTY = new HeadAndTail(Collections.emptyList());
    private final List<E> list;

    public HeadAndTail(List<E> list) {
        this.list = list;
    }

    public E head() {
        return list.get(0);
    }

    public HeadAndTail<E> tail() {
        if (list.isEmpty()) {
            return empty();
        }
        return new HeadAndTail<>(list.subList(1, list.size()));
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public static <E> HeadAndTail<E> empty() {
        return EMPTY;
    }
}
