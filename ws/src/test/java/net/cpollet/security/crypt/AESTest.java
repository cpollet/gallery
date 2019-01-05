package net.cpollet.security.crypt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AESTest {
    @Test
    void encryptThenDecrypt_returnsSameText() {
        // GIVEN
        AESCleartext clearText = new AESCleartext("some text".getBytes());

        // WHEN
        AESCiphertext cipherText = clearText.encrypt();

        // THEN
        Assertions.assertEquals("some text", new String(cipherText.clearText().clearText()));
    }

    @Test
    void decryptEncrypted_returnsSameText() {
        // GIVEN
        AESCleartext clearText = new AESCleartext("some text".getBytes());

        // WHEN
        AESCiphertext encrypted = clearText.encrypt();
        AESCiphertext toDecrypt = new AESCiphertext(encrypted.cipherText(), encrypted.key());

        // THEN
        Assertions.assertEquals("some text", new String(toDecrypt.clearText().clearText()));
    }

    @Test
    void decryptEncryptedWithKey_returnsSameText() {
        // GIVEN
        AESKey key = new AESKey();
        AESCleartext clearText = new AESCleartext("some text".getBytes(), key);

        // WHEN
        AESCiphertext encrypted = clearText.encrypt();
        AESCiphertext toDecrypt = new AESCiphertext(encrypted.cipherText(), key);

        // THEN
        Assertions.assertEquals("some text", new String(toDecrypt.clearText().clearText()));
    }
}