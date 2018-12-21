package net.cpollet.security.hash;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Base64;

class Sha256HashTest {
    @Test
    void hash_returnsHash() {
        // GIVEN
        Hash hash = new Sha256Hash();

        // WHEN
        byte[] hashed = hash.hash("clear".getBytes());

        // THEN
        Assertions.assertEquals("kTpMuRviAzLzVZ+AcCVdesPmIou0I/REFVHT94Pn1PQ=", Base64.getEncoder().encodeToString(hashed));
    }
}
