package net.cpollet.security.crypt;

import net.cpollet.kozan.lazy.Lazy;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public final class AESCiphertext {
    private final AESKey key;
    private final byte[] text;
    private final Lazy<AESCleartext> result;

    public AESCiphertext(byte[] text, AESKey key) {
        this.key = key;
        this.text = text;
        this.result = new Lazy<>(this::performDecryption);
    }

    private AESCleartext performDecryption() {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(text);
            return new AESCleartext(
                    decryptionCipher(
                            iv(byteBuffer),
                            key.toBytes()
                    ).doFinal(cipherText(byteBuffer))
            );
        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

    private byte[] iv(ByteBuffer byteBuffer) {
        int ivLength = byteBuffer.getInt();
        if (ivLength < 12 || ivLength >= 16) {
            throw new IllegalArgumentException("invalid iv length");
        }
        byte[] iv = new byte[ivLength];
        byteBuffer.get(iv);
        return iv;
    }

    private byte[] cipherText(ByteBuffer byteBuffer) {
        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);
        return cipherText;
    }

    private Cipher decryptionCipher(byte[] iv, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, iv));
        return cipher;
    }

    public AESCleartext clearText() {
        return result.value();
    }

    public byte[] cipherText() {
        return text;
    }

    public AESKey key() {
        return key;
    }
}
