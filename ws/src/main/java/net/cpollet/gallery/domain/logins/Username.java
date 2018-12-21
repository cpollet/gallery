package net.cpollet.gallery.domain.logins;

import net.cpollet.gallery.domain.ValidationException;

import java.util.Objects;

public final class Username {
    private static final String VALIDATION_TARGET = "user.username";
    private final String name;

    public Username(String name) {
        if (name == null) {
            throw ValidationException.isNull(VALIDATION_TARGET);
        }
        if (name.isEmpty()) {
            throw ValidationException.isEmpty(VALIDATION_TARGET);
        }
        if (name.length() > 20) {
            throw ValidationException.isLongerThan(VALIDATION_TARGET, 20);
        }
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username username1 = (Username) o;
        return Objects.equals(name, username1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
