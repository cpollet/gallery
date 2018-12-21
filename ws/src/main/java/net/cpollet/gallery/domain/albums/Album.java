package net.cpollet.gallery.domain.albums;

import net.cpollet.gallery.domain.Unwrappable;
import net.cpollet.gallery.domain.tags.Tagged;

public interface Album extends Tagged, Unwrappable<Album> {
    long id();

    AlbumName name();

    Album name(AlbumName name);

    AlbumDescription description();

    Album description(AlbumDescription description);

    boolean published();

    Album published(boolean published);
}
