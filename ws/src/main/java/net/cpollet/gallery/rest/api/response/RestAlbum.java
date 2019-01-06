package net.cpollet.gallery.rest.api.response;

import net.cpollet.gallery.domain.albums.Album;
import net.cpollet.gallery.domain.albums.AlbumDescription;
import net.cpollet.gallery.domain.albums.AlbumName;

import java.util.Collections;
import java.util.List;

public final class RestAlbum {
    private Long id;
    private String name;
    private String description;
    private boolean published;
    private List<Link> links;

    public RestAlbum() {
        // noop
    }

    public RestAlbum(Long id, AlbumName name, AlbumDescription description, boolean published, List<Link> links) {
        this.id = id;
        this.name = name.toString();
        this.description = description.toString();
        this.published = published;
        this.links = links;
    }

    public RestAlbum(Album album, List<Link> links) {
        this(
                album.id(),
                album.name(),
                album.description(),
                album.published(),
                links
        );
    }

    public RestAlbum(Album album) {
        this(
                album,
                Collections.emptyList()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
