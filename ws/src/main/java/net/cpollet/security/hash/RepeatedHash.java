package net.cpollet.security.hash;

public final class RepeatedHash extends AbstractHash {
    private final Hash wrapped;
    private final int count;

    public RepeatedHash(Hash wrapped, int count) {
        this.wrapped = wrapped;
        this.count = count;
    }

    @Override
    public byte[] hash(byte[] bytes) {
        byte[] result = wrapped.hash(bytes);
        for (int i = 1; i < count; i++) {
            result = wrapped.hash(result);
        }
        return result;
    }
}
