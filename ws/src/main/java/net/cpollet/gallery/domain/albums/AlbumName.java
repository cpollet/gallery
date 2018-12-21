package net.cpollet.gallery.domain.albums;

import net.cpollet.gallery.domain.ValidationException;

import java.util.Objects;

public final class AlbumName implements Comparable<AlbumName> {
    private static final String VALIDATION_TARGET = "album.name";
    private final String name;

    public AlbumName(String name) {
        if (name == null) {
            throw ValidationException.isNull(VALIDATION_TARGET);
        }
        if (name.isEmpty()) {
            throw ValidationException.isEmpty(VALIDATION_TARGET);
        }
        if (name.length() > 50) {
            throw ValidationException.isLongerThan(VALIDATION_TARGET, 50);
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
        AlbumName albumName = (AlbumName) o;
        return name.equals(albumName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(AlbumName o) {
        return name.compareToIgnoreCase(o.name);
    }
}
