package net.cpollet.gallery.domain.albums;

import java.util.List;

public interface Albums {
    Album create(AlbumName name);

    Album findById(long id);

    Album loadById(long id);

    List<Album> findAll();

    List<CachedAlbum> loadAll();
}
