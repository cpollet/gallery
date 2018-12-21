package net.cpollet.gallery.domain.albums;

import com.google.common.collect.ImmutableMap;
import com.twitter.twittertext.Extractor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.cpollet.gallery.database.Database;
import net.cpollet.gallery.domain.albums.exceptions.AlbumNotFoundException;
import net.cpollet.gallery.utils.TagExtractor;
import net.cpollet.gallery.utils.TwitterTagExtractor;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("squid:S1192") // we want full string for readability
@ToString
@Slf4j
public final class PgAlbum implements Album {
    private final Database database;
    private final TagExtractor tagExtractor;
    private final long id;

    PgAlbum(Database database, long id) {
        this(database, id, new TwitterTagExtractor(new Extractor()));
    }

    PgAlbum(Database database, long id, TagExtractor tagExtractor) {
        this.tagExtractor = tagExtractor;
        this.id = id;
        this.database = database;
    }

    @Override
    public long id() {
        return id;
    }

    @Override
    public AlbumName name() {
        return new AlbumName(new PgReadableAlbums(database, "SELECT id, name FROM albums WHERE id=:id")
                .toQuery()
                .with(ImmutableMap.of(
                        "id", id
                ))
                .fetch((rs, rowNum) -> rs.getString("name"))
                .stream()
                .findFirst()
                .orElseThrow(() -> new AlbumNotFoundException(id)));
    }

    @Override
    public Album name(AlbumName name) {
        database.query("UPDATE albums SET name=:name WHERE id=:id") // FIXME implement filter
                .with(ImmutableMap.of(
                        "name", name.toString(),
                        "id", id
                ))
                .execute();

        return this;
    }

    @Override
    public AlbumDescription description() {
        return new AlbumDescription(new PgReadableAlbums(database, "SELECT id, description FROM albums WHERE id=:id")
                .toQuery()
                .with(ImmutableMap.of(
                        "id", id
                ))
                .fetch((rs, rowNum) -> rs.getString("description"))
                .stream()
                .findFirst()
                .orElseThrow(() -> new AlbumNotFoundException(id)));
    }

    @Override
    public Album description(AlbumDescription description) {
        database.query("UPDATE albums SET description=:description WHERE id=:id") // FIXME implement filter
                .with(ImmutableMap.of(
                        "description", description.toString(),
                        "id", id
                ))
                .execute();

        return this;
    }

    @Override
    public boolean published() {
        return Boolean.TRUE.equals(new PgReadableAlbums(database, "SELECT id, published FROM albums WHERE id=:id")
                .toQuery()
                .with(ImmutableMap.of(
                        "id", id
                ))
                .fetch((rs, rowNum) -> rs.getBoolean("published"))
                .stream()
                .findFirst()
                .orElseThrow(() -> new AlbumNotFoundException(id)));
    }

    @Override
    public Album published(boolean published) {
        database.query("UPDATE albums SET published=:published WHERE id=:id") // FIXME implement filter
                .with(ImmutableMap.of(
                        "published", published,
                        "id", id
                ))
                .execute();

        return this;
    }

    @Override
    public List<String> tags() {
        return tagExtractor.extractTags(description().toString()).stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Album> T unwrap(Class<T> clazz) {
        if (clazz.isInstance(this)) {
            return (T) this;
        }
        throw new IllegalArgumentException("Cannot unwrap to " + clazz.getName());
    }


}




