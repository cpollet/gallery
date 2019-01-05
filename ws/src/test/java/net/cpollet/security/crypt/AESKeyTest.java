package net.cpollet.security.crypt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AESKeyTest {
    @Test
    void newKey_generatesA128bitsKey() {
        //GIVEN
        AESKey key = new AESKey();

        // WHEN
        byte[] bytes = key.toBytes();

        //THEN
        Assertions.assertEquals(128, toBits(bytes.length));
    }

    @Test
    void newKey_throwsException_whenKeySizeIsInvalid() {
        try {
            new AESKey(new byte[7]);
            Assertions.fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Invalid key length: 7 bytes, expected: [16, 24, 32] bytes", e.getMessage());
        }
    }

    @Test
    void newKey_doesNotThrowException_whenKeySizeIsValid() {
        new AESKey(new byte[16]);
        new AESKey(new byte[24]);
        new AESKey(new byte[32]);
    }

    private int toBits(int bytes) {
        return bytes * 8;
    }
}