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
import java.security.SecureRandom;

public final class AESCleartext {
    private final byte[] text;
    private final AESKey key;
    private final Lazy<AESCiphertext> result;

    public AESCleartext(byte[] text, AESKey key) {
        this.text = text;
        this.key = key;
        this.result = new Lazy<>(this::performEncryption);
    }

    public AESCleartext(byte[] text) {
        this(text, new AESKey());
    }

    private AESCiphertext performEncryption() {
        try {
            byte[] iv = iv();
            return new AESCiphertext(
                    concat(
                            iv,
                            encryptionCipher(iv, key.toBytes()).doFinal(text)
                    )
                    ,
                    key
            );
        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

    private static byte[] iv() throws NoSuchAlgorithmException {
        byte[] iv = new byte[12];
        SecureRandom.getInstanceStrong().nextBytes(iv);
        return iv;
    }

    private byte[] concat(byte[] iv, byte[] cipher) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + cipher.length);
        byteBuffer.putInt(iv.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipher);
        return byteBuffer.array();
    }

    private Cipher encryptionCipher(byte[] iv, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, iv));
        return cipher;
    }

    public AESCiphertext encrypt() {
        return result.value();
    }

    public byte[] clearText() {
        return text;
    }
}
