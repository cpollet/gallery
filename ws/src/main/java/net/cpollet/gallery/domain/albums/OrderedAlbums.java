package net.cpollet.gallery.domain.albums;

import java.util.List;
import java.util.stream.Collectors;

public final class OrderedAlbums implements Albums {
    private final Albums albums;
    private final AlbumComparator comparator;

    public OrderedAlbums(Albums albums, AlbumComparator comparator) {
        this.albums = albums;
        this.comparator = comparator;
    }

    @Override
    public Album create(AlbumName name) {
        return albums.create(name);
    }

    @Override
    public Album findById(long id) {
        return albums.findById(id);
    }

    @Override
    public Album loadById(long id) {
        return albums.loadById(id);
    }

    @Override
    public List<Album> findAll() {
        return albums.findAll().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public List<CachedAlbum> loadAll() {
        return albums.loadAll().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
