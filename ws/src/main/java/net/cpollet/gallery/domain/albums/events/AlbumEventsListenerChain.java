package net.cpollet.gallery.domain.albums.events;

import net.cpollet.gallery.domain.albums.Album;
import net.cpollet.gallery.domain.albums.AlbumDescription;
import net.cpollet.gallery.domain.albums.AlbumName;

import java.util.List;

public final class AlbumEventsListenerChain implements AlbumEventsListener {
    private final List<AlbumEventsListener> listeners;

    public AlbumEventsListenerChain(List<AlbumEventsListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void nameUpdated(Album album, AlbumName oldName) {
        listeners.forEach(l -> l.nameUpdated(album, oldName));
    }

    @Override
    public void descriptionUpdated(Album album, AlbumDescription oldDescription) {
        listeners.forEach(l -> l.descriptionUpdated(album, oldDescription));
    }

    @Override
    public void publishedUpdated(Album album, boolean oldPublished) {
        listeners.forEach(l -> l.publishedUpdated(album, oldPublished));
    }

    @Override
    public void tagsUpdated(Album album, List<String> oldTags) {
        listeners.forEach(l -> l.tagsUpdated(album, oldTags));
    }
}
