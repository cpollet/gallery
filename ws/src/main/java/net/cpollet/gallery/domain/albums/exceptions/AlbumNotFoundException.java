package net.cpollet.gallery.domain.albums.exceptions;

public final class AlbumNotFoundException extends RuntimeException {
    public AlbumNotFoundException(Long id) {
        super(String.format("Album [%d] does not exist", id));
    }
}
