package net.cpollet.kozan.collections;

import lombok.Value;

@Value(staticConstructor = "of")
public final class Pair<K, V> {
    K key;
    V value;
}