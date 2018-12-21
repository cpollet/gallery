package net.cpollet.gallery.domain.albums;

import lombok.RequiredArgsConstructor;
import net.cpollet.gallery.domain.albums.events.AlbumEventsListener;

import java.util.List;

@RequiredArgsConstructor
public final class ObservableAlbum implements Album {
    private final Album album;
    private final AlbumEventsListener eventListener;

    @Override
    public long id() {
        return album.id();
    }

    @Override
    public AlbumName name() {
        return album.name();
    }

    @Override
    public ObservableAlbum name(AlbumName name) {
        AlbumName oldName = name();
        album.name(name);
        if (!oldName.equals(album.name())) {
            eventListener.nameUpdated(album, oldName);
        }
        return this;
    }

    @Override
    public AlbumDescription description() {
        return album.description();
    }

    @Override
    public ObservableAlbum description(AlbumDescription description) {
        AlbumDescription oldDescription = description();
        List<String> oldTags = tags();
        album.description(description);
        if (!oldDescription.equals(description())) {
            eventListener.descriptionUpdated(album, oldDescription);
        }
        if (!oldTags.equals(tags())) {
            eventListener.tagsUpdated(album, oldTags);
        }
        return this;
    }

    @Override
    public boolean published() {
        return album.published();
    }

    @Override
    public ObservableAlbum published(boolean published) {
        boolean oldPublished = published();
        album.published(published);
        if (oldPublished != published()) {
            eventListener.publishedUpdated(album, oldPublished);
        }
        return this;
    }

    @Override
    public List<String> tags() {
        return album.tags();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Album> T unwrap(Class<T> clazz) {
        if (clazz.isInstance(this)) {
            return (T) this;
        }
        return album.unwrap(clazz);
    }
}
