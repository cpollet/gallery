package net.cpollet.kozan.generator;

public final class IntegerIncrementGenerator implements Generator<Integer> {
    private final SequenceGenerator<Integer> generator;

    public IntegerIncrementGenerator(int from, int increment) {
        generator = new SequenceGenerator<>(from, i -> i + increment);
    }

    @Override
    public boolean hasNext() {
        return generator.hasNext();
    }

    @Override
    public Integer next() {
        return generator.next();
    }
}
