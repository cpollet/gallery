package net.cpollet.gallery.domain.albums;

import net.cpollet.gallery.domain.albums.exceptions.AlbumNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public final class FilteredAlbums implements Albums {
    private final Albums albums;
    private final AlbumFilter filter;

    public FilteredAlbums(Albums albums, AlbumFilter filter) {
        this.albums = albums;
        this.filter = filter;
    }

    @Override
    public Album create(AlbumName name) {
        Album album = albums.create(name);

        if (!filter.test(album)) {
            throw new AlbumNotFoundException(album.id());
        }

        return album;
    }

    @Override
    public Album findById(long id) {
        Album album = albums.findById(id);

        if (!filter.test(album)) {
            throw new AlbumNotFoundException(album.id());
        }

        return album;
    }

    @Override
    public Album loadById(long id) {
        Album album = albums.loadById(id);

        if (!filter.test(album)) {
            throw new AlbumNotFoundException(album.id());
        }

        return album;
    }

    @Override
    public List<Album> findAll() {
        return albums.findAll().stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    @Override
    public List<CachedAlbum> loadAll() {
        return albums.loadAll().stream()
                .filter(filter)
                .collect(Collectors.toList());
    }
}
