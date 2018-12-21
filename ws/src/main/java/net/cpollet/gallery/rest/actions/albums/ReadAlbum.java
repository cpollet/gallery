package net.cpollet.gallery.rest.actions.albums;

import net.cpollet.gallery.domain.albums.Album;
import net.cpollet.gallery.domain.albums.exceptions.AlbumNotFoundException;
import net.cpollet.gallery.domain.gallery.Gallery;
import net.cpollet.gallery.rest.api.response.Link;
import net.cpollet.gallery.rest.api.response.RestAlbum;
import net.cpollet.gallery.rest.core.Action;
import net.cpollet.gallery.rest.core.ActionException;
import net.cpollet.gallery.rest.core.ActionUrlTemplate;
import net.cpollet.gallery.rest.core.Response;

import java.util.Collections;
import java.util.List;

public final class ReadAlbum implements Action {
    private final Gallery gallery;
    private final Payload payload;
    private final ActionUrlTemplate albumUrlTemplate;

    public ReadAlbum(Gallery gallery, Payload payload, ActionUrlTemplate albumUrlTemplate) {
        this.gallery = gallery;
        this.payload = payload;
        this.albumUrlTemplate = albumUrlTemplate;
    }

    @Override
    public Response execute() {
        try {
            Album album = gallery.albums().loadById(payload.id());
            return Response.ok(new RestAlbum(album, buildLinks(album)));
        } catch (AlbumNotFoundException e) {
            throw ActionException.NOT_FOUND;
        }
    }

    private List<Link> buildLinks(Album album) {
        return Collections.singletonList(
                new Link(albumUrlTemplate.apply(album.id()), "GET", "album")
        );
    }

    public interface Payload {
        Long id();
    }

    public static class SerializedPayload implements Payload {
        private final String string;

        public SerializedPayload(String string) {
            this.string = string;
        }

        @Override
        public Long id() {
            try {
                return Long.parseLong(string);
            } catch (NumberFormatException e) {
                throw ActionException.NOT_FOUND;
            }
        }
    }
}
