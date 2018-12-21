package net.cpollet.gallery.rest.api.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.cpollet.gallery.domain.albums.Album;
import net.cpollet.gallery.domain.albums.AlbumDescription;
import net.cpollet.gallery.domain.albums.AlbumName;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Data
public final class RestAlbum {
    private Long id;
    private String name;
    private String description;
    private boolean published;
    private List<Link> links;

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
}
