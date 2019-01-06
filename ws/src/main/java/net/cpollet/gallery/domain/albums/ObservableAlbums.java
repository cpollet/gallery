package net.cpollet.gallery.domain.albums;

import net.cpollet.gallery.domain.albums.events.AlbumsEventsListener;

import java.util.List;

public final class ObservableAlbums implements Albums {
    private final Albums albums;
    private final AlbumsEventsListener eventsListener;

    public ObservableAlbums(Albums albums, AlbumsEventsListener eventsListener) {
        this.albums = albums;
        this.eventsListener = eventsListener;
    }

    @Override
    public Album create(AlbumName name) {
        Album album = albums.create(name);
        eventsListener.albumCreated(album);
        return album;
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
        return albums.findAll();
    }

    @Override
    public List<CachedAlbum> loadAll() {
        return albums.loadAll();
    }
}
