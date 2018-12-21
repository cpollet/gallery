package net.cpollet.gallery.domain.logins;

import net.cpollet.gallery.domain.ValidationException;
import net.cpollet.security.hash.Hash;
import net.cpollet.security.hash.RepeatedHash;
import net.cpollet.security.hash.SaltedHash;
import net.cpollet.security.hash.Sha256Hash;

import java.util.Arrays;
import java.util.Base64;

public final class Password {
    private static final String VALIDATION_TARGET = "user.password";
    private final byte[] salt;
    private final Hash hash;
    private final byte[] hashedPassword;

    public Password(String hashedPassword) {
        if (hashedPassword == null) {
            throw ValidationException.isNull(VALIDATION_TARGET);
        }
        if (hashedPassword.indexOf(':') == -1) {
            throw ValidationException.isInvalid(VALIDATION_TARGET, hashedPassword);
        }

        this.salt = Base64.getDecoder().decode(hashedPassword.substring(0, hashedPassword.indexOf(':')));
        this.hash = hash();
        this.hashedPassword = Base64.getDecoder().decode(hashedPassword.substring(hashedPassword.indexOf(':') + 1));
    }

    private Hash hash() {
        return new RepeatedHash(
                new SaltedHash(
                        new Sha256Hash(),
                        salt::clone
                ),
                100
        );
    }

    public Password(char[] clearPassword) {
        if (clearPassword == null) {
            throw ValidationException.isNull(VALIDATION_TARGET);
        }
        if (clearPassword.length == 0) {
            throw ValidationException.isEmpty(VALIDATION_TARGET);
        }

        this.salt = new SaltedHash.DefaultSaltGenerator().generate();
        this.hash = hash();
        this.hashedPassword = hash.hash(clearPassword);
    }

    public boolean matches(char[] password) {
        return Arrays.equals(hash.hash(password), hashedPassword);
    }

    @Override
    public String toString() {
        return String.format("%s:%s",
                Base64.getEncoder().encodeToString(salt),
                Base64.getEncoder().encodeToString(hashedPassword)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Arrays.equals(hashedPassword, password.hashedPassword);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hashedPassword);
    }
}
