package net.cpollet.security.hash;

import java.nio.charset.StandardCharsets;

public final class NoHash extends AbstractHash {
    @Override
    public byte[] hash(byte[] bytes) {
        return ("H(" + new String(bytes, StandardCharsets.UTF_8) + ")").getBytes();
    }
}
