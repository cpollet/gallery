package net.cpollet.gallery.domain.albums;

import net.cpollet.gallery.domain.ValidationException;

import java.util.Objects;

public final class AlbumDescription {
    private static final String VALIDATION_TARGET = "album.description";
    private final String description;
    static final AlbumDescription EMPTY = new AlbumDescription("");

    public AlbumDescription(String description) {
        if (description == null) {
            throw ValidationException.isNull(VALIDATION_TARGET);
        }
        if (description.length() > 1000) {
            throw ValidationException.isLongerThan(VALIDATION_TARGET, 1000);
        }
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlbumDescription that = (AlbumDescription) o;
        return description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
