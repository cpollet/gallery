package net.cpollet.security.hash;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RepeatedHashTest {
    @Test
    void hash_returnsHash() {
        // GIVEN
        Hash hash = new RepeatedHash(
                new NoHash(),
                2
        );

        // WHEN
        byte[] hashed = hash.hash("clear".getBytes());

        // THEN
        Assertions.assertArrayEquals("H(H(clear))".getBytes(), hashed);
    }
}
