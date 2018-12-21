package net.cpollet.gallery.domain.gallery;

import net.cpollet.gallery.domain.logins.Logins;
import net.cpollet.gallery.domain.albums.Albums;
import net.cpollet.gallery.domain.tags.Tags;

public interface Gallery {
    Albums albums();

    Tags tags();

    Logins logins();
}
