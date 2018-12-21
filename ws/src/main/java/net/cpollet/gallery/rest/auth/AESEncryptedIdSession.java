package net.cpollet.gallery.rest.auth;

import net.cpollet.security.crypt.AESCiphertext;
import net.cpollet.security.crypt.AESCleartext;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.function.Function;

public final class AESEncryptedIdSession implements Sessions {
    private final Sessions wrapped;
    private final Function<String, String> encryptionAlgorithm;
    private final Function<String, String> decryptionAlgorithm;

    public AESEncryptedIdSession(byte[] encryptionKey, Sessions wrapped) {
        this.wrapped = wrapped;
        this.encryptionAlgorithm = encryptionAlgorithm(encryptionKey);
        this.decryptionAlgorithm = decryptionAlgorithm(encryptionKey);
    }

    private Function<String, String> encryptionAlgorithm(byte[] sessionIdEncryptionKey) {
        return s -> Base64.getEncoder().encodeToString(
                new AESCleartext(
                        s.getBytes(StandardCharsets.UTF_8),
                        sessionIdEncryptionKey
                ).encrypt().cipherText()
        );
    }

    private Function<String, String> decryptionAlgorithm(byte[] sessionIdEncryptionKey) {
        return s -> new String(
                new AESCiphertext(
                        Base64.getDecoder().decode(s.getBytes(StandardCharsets.UTF_8)),
                        sessionIdEncryptionKey
                ).clearText().clearText(),
                StandardCharsets.UTF_8
        );
    }

    @Override
    public String create(String username) {
        return encryptionAlgorithm.apply(wrapped.create(username));
    }

    @Override
    public Optional<String> load(String sessionId) {
        return wrapped.load(decryptionAlgorithm.apply(sessionId));
    }
}
