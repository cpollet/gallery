package net.cpollet.security.crypt;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

public final class AESKey {
    private static final List<Integer> validLengths = List.of(128 / 8, 192 / 8, 256 / 8);
    private final byte[] key;

    public AESKey(byte[] key) {
        if (!validLengths.contains(key.length)) {
            throw new IllegalArgumentException(String.format("Invalid key length: %s bytes, expected: %s bytes", key.length, validLengths));
        }
        this.key = key;
    }

    public AESKey() {
        this(generateKey());
    }

    private static byte[] generateKey() {
        try {
            byte[] key = new byte[16];
            SecureRandom.getInstanceStrong().nextBytes(key);
            return key;
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException("Unable to generate key", e);
        }
    }


    byte[] toBytes() {
        return key;
    }
}
