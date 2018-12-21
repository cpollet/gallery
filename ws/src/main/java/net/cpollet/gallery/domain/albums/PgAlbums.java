package net.cpollet.gallery.domain.albums;

import com.google.common.collect.ImmutableMap;
import net.cpollet.gallery.database.Database;
import net.cpollet.gallery.domain.albums.exceptions.AlbumNotFoundException;

import java.util.List;

@SuppressWarnings("squid:S1192") // we want full string for readability
public final class PgAlbums implements Albums {
    private final Database database;

    public PgAlbums(Database database) {
        this.database = database;
    }

    @Override
    public Album create(AlbumName name) {
        final AlbumDescription description = AlbumDescription.EMPTY;
        final boolean published = false;

        Long albumId = database.query("INSERT INTO albums (name, description, published) VALUES (:name, :description, :published) RETURNING id")
                .with(ImmutableMap.of(
                        "name", name.toString(),
                        "description", description.toString(),
                        "published", published
                ))
                .fetch(Long.class)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to create album"));

        return new CachedAlbum(new PgAlbum(database, albumId), name, description, published);
    }

    @Override
    public Album findById(long id) {
        return new PgReadableAlbums(database, "SELECT id FROM albums WHERE id=:id")
                .toQuery()
                .with(ImmutableMap.of(
                        "id", id
                ))
                .fetch((rs, rowNum) -> new CachedAlbum(new PgAlbum(
                        database,
                        rs.getLong("id"))))
                .stream()
                .findFirst()
                .orElseThrow(() -> new AlbumNotFoundException(id));
    }

    @Override
    public CachedAlbum loadById(long id) {
        return new PgReadableAlbums(database, "SELECT id, name, description, published FROM albums WHERE id=:id")
                .toQuery()
                .with(ImmutableMap.of(
                        "id", id
                ))
                .fetch((rs, rowNum) -> new CachedAlbum(
                        new PgAlbum(
                                database,
                                rs.getLong("id")),
                        new AlbumName(rs.getString("name")),
                        new AlbumDescription(rs.getString("description")),
                        rs.getBoolean("published")
                ))
                .stream()
                .findFirst()
                .orElseThrow(() -> new AlbumNotFoundException(id));
    }

    @Override
    public List<Album> findAll() {
        return new PgReadableAlbums(database, "SELECT id FROM albums")
                .toQuery()
                .fetch((rs, rowNum) -> new CachedAlbum(new PgAlbum(
                        database,
                        rs.getLong("id"))
                ));
    }

    @Override
    public List<CachedAlbum> loadAll() {
        return new PgReadableAlbums(database, "SELECT id, name, description, published FROM albums")
                .toQuery()
                .fetch((rs, rowNum) -> new CachedAlbum(
                        new PgAlbum(
                                database,
                                rs.getLong("id")
                        ),
                        new AlbumName(rs.getString("name")),
                        new AlbumDescription(rs.getString("description")),
                        rs.getBoolean("published")
                ));
    }
}
