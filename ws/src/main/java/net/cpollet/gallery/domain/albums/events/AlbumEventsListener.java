package net.cpollet.gallery.domain.albums.events;

import net.cpollet.gallery.domain.albums.Album;
import net.cpollet.gallery.domain.albums.AlbumDescription;
import net.cpollet.gallery.domain.albums.AlbumName;

import java.util.List;

public interface AlbumEventsListener {
    void nameUpdated(Album album, AlbumName oldName);

    void descriptionUpdated(Album album, AlbumDescription oldDescription);

    void publishedUpdated(Album album, boolean oldPublished);

    void tagsUpdated(Album album, List<String> oldTags);
}
