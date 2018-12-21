package net.cpollet.security.hash;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NoHashTest {
     @Test
    void hash_returnsHash() {
         // GIVEN
         Hash hash = new NoHash();

         // WHEN
         byte[] hashed = hash.hash("clear".getBytes());

         // THEN
         Assertions.assertArrayEquals("H(clear)".getBytes(), hashed);
     }

    @Test
    void hashChar_returnsHash() {
        // GIVEN
        Hash hash = new NoHash();

        // WHEN
        byte[] hashed = hash.hash("clear".toCharArray());

        // THEN
        Assertions.assertArrayEquals("H(clear)".getBytes(), hashed);
    }
}
