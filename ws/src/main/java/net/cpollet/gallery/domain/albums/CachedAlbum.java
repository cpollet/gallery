package net.cpollet.gallery.domain.albums;

import net.cpollet.kozan.lazy.Lazy;

import java.util.List;

public final class CachedAlbum implements Album {
    private final Album album;
    private Lazy<AlbumName> name;
    private Lazy<AlbumDescription> description;
    private Lazy<Boolean> published;
    private Lazy<List<String>> tags;

    private CachedAlbum(Album album, Lazy<AlbumName> name, Lazy<AlbumDescription> description, Lazy<Boolean> published) {
        this.album = album;
        this.name = name;
        this.description = description;
        this.published = published;
        this.tags = new Lazy<>(album::tags);
    }

    CachedAlbum(Album album) {
        this(
                album,
                new Lazy<>(album::name),
                new Lazy<>(album::description),
                new Lazy<>(album::published)
        );
    }

    CachedAlbum(Album album, AlbumName name, AlbumDescription description, Boolean published) {
        this(
                album,
                new Lazy<>(name),
                new Lazy<>(description),
                new Lazy<>(published)
        );
    }

    @Override
    public long id() {
        return album.id();
    }

    @Override
    public AlbumName name() {
        return name.value();
    }

    @Override
    public CachedAlbum name(AlbumName name) {
        if (!this.name.hasValue() || !this.name.value().equals(name)) {
            album.name(name);
            this.name = new Lazy<>(name);
        }
        return this;
    }

    @Override
    public AlbumDescription description() {
        return description.value();
    }

    @Override
    public CachedAlbum description(AlbumDescription description) {
        if (!this.description.hasValue() || !this.description.value().equals(description)) {
            album.description(description);
            // we update both as we want to make sure they're synchronized
            this.description = new Lazy<>(description);
            this.tags = new Lazy<>(album.tags());
        }
        return this;
    }

    @Override
    public boolean published() {
        return published.value();
    }

    @Override
    public CachedAlbum published(boolean published) {
        if (!this.published.hasValue() || !this.published.value().equals(published)) {
            album.published(published);
            this.published = new Lazy<>(published);
        }
        return this;
    }

    @Override
    public List<String> tags() {
        this.description = new Lazy<>(album::description);
        return tags.value();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Album> T unwrap(Class<T> clazz) {
        if (clazz.isInstance(this)) {
            return (T) this;
        }
        return album.unwrap(clazz);
    }

    public CachedAlbum evict() {
        name = new Lazy<>(album::name);
        description = new Lazy<>(album::description);
        published = new Lazy<>(album::published);
        tags = new Lazy<>(album::tags);
        return this;
    }

    public Album direct() {
        return album;
    }
}
