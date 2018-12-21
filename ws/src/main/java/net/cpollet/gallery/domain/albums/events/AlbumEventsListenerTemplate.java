package net.cpollet.gallery.domain.albums.events;

import net.cpollet.gallery.domain.albums.Album;
import net.cpollet.gallery.domain.albums.AlbumDescription;
import net.cpollet.gallery.domain.albums.AlbumName;

import java.util.List;

public class AlbumEventsListenerTemplate implements AlbumEventsListener {
    @Override
    public void nameUpdated(Album album, AlbumName oldName) {
        // noop
    }

    @Override
    public void descriptionUpdated(Album album, AlbumDescription oldDescription) {
        // noop
    }

    @Override
    public void publishedUpdated(Album album, boolean oldPublished) {
        // noop
    }

    @Override
    public void tagsUpdated(Album album, List<String> oldTags) {
        // noop
    }
}
