package net.cpollet.kozan.generator;

import java.util.concurrent.ThreadLocalRandom;

public final class RandomIntegerGenerator implements Generator<Integer> {
    private final int min;
    private final int max;

    public RandomIntegerGenerator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    @SuppressWarnings("squid:S2272") // we don't have a "last value"
    public Integer next() {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
