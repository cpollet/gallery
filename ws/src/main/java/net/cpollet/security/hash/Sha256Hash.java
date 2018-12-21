package net.cpollet.security.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Sha256Hash extends AbstractHash {
    @Override
    public byte[] hash(byte[] bytes) {
        return sha256().digest(bytes);
    }

    private MessageDigest sha256() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unable to initialize SHA-256 algorithm", e);
        }
    }
}
