package net.cpollet.gallery.domain.gallery;

import net.cpollet.gallery.database.Database;
import net.cpollet.gallery.domain.albums.Albums;
import net.cpollet.gallery.domain.albums.PgAlbums;
import net.cpollet.gallery.domain.logins.Logins;
import net.cpollet.gallery.domain.logins.PgLogins;
import net.cpollet.gallery.domain.tags.PgTags;
import net.cpollet.gallery.domain.tags.Tags;

public final class PgGallery implements Gallery {
    private final Albums albums;
    private final Tags tags;
    private final Logins logins;

    public PgGallery(Database database) {
        albums = new PgAlbums(database);
        tags = new PgTags(database);
        logins = new PgLogins(database);
    }

    @Override
    public Albums albums() {
        return albums;
    }

    @Override
    public Tags tags() {
        return tags;
    }

    @Override
    public Logins logins() {
        return logins;
    }
}
