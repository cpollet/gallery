package net.cpollet.security.hash;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public final class SaltedHash extends AbstractHash {
    private final Hash wrapped;
    private final SaltGenerator saltGenerator;

    public SaltedHash(Hash wrapped, SaltGenerator saltGenerator) {
        this.wrapped = wrapped;
        this.saltGenerator = saltGenerator;
    }

    public SaltedHash(Hash wrapped, int saltSize) {
        this(
                wrapped,
                new DefaultSaltGenerator(saltSize)
        );
    }

    public SaltedHash(Hash wrapped) {
        this(
                wrapped,
                new DefaultSaltGenerator()
        );
    }

    @Override
    public byte[] hash(byte[] bytes) {
        byte[] salt = saltGenerator.generate();

        try {
            return wrapped.hash(
                    merge(
                            salt,
                            bytes
                    )
            );
        } finally {
            Arrays.fill(salt,(byte)0);
        }
    }

    private byte[] merge(byte[] array1, byte[] array2) {
        byte[] result = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public interface SaltGenerator {
        byte[] generate();
    }

    public static class DefaultSaltGenerator implements SaltGenerator {
        private final int length;

        public DefaultSaltGenerator(int length) {
            this.length = length;
        }

        public DefaultSaltGenerator() {
            this(12);
        }

        @Override
        public byte[] generate() {
            try {
                byte[] bytes = new byte[length];
                SecureRandom.getInstanceStrong().nextBytes(bytes);
                return bytes;
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("Unable to generate secure bytes", e);
            }
        }
    }
}
