package net.cpollet.security.hash;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

 class SaltedHashTest {
    @Test
    void hash_returnsHash() {
        // GIVEN
        Hash hash = new SaltedHash(
                new NoHash(),
                "_"::getBytes
        );

        // WHEN
        byte[] hashed = hash.hash("clear".getBytes());

        // THEN
        Assertions.assertArrayEquals("H(_clear)".getBytes(), hashed);
    }
}
