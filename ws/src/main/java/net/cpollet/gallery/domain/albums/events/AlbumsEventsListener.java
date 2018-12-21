package net.cpollet.gallery.domain.albums.events;

import net.cpollet.gallery.domain.albums.Album;

public interface AlbumsEventsListener {
    void albumCreated(Album album);
}
