package net.cpollet.gallery.rest.actions.albums;

import net.cpollet.gallery.domain.ValidationException;
import net.cpollet.gallery.domain.albums.Album;
import net.cpollet.gallery.domain.albums.AlbumComparator;
import net.cpollet.gallery.domain.albums.AlbumFilter;
import net.cpollet.gallery.domain.albums.Albums;
import net.cpollet.gallery.domain.albums.FilteredAlbums;
import net.cpollet.gallery.domain.albums.OrderedAlbums;
import net.cpollet.gallery.domain.gallery.Gallery;
import net.cpollet.gallery.rest.api.request.ValidatablePayload;
import net.cpollet.gallery.rest.api.response.Link;
import net.cpollet.gallery.rest.api.response.RestAlbum;
import net.cpollet.gallery.rest.core.Action;
import net.cpollet.gallery.rest.core.ActionUrlTemplate;
import net.cpollet.gallery.rest.core.Response;
import net.cpollet.kozan.collections.HeadAndTail;
import net.cpollet.kozan.lazy.Lazy;
import net.cpollet.kozan.maybe.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ListAlbums implements Action {
    private static final Logger log = LoggerFactory.getLogger(ListAlbums.class);

    private final Gallery gallery;
    private final Payload payload;
    private final ActionUrlTemplate albumUrlTemplate;

    public ListAlbums(Gallery gallery, Payload payload, ActionUrlTemplate albumUrlTemplate) {
        this.gallery = gallery;
        this.payload = payload;
        this.albumUrlTemplate = albumUrlTemplate;
    }

    @Override
    public Response execute() {
        Albums albums = sort(
                filter(
                        gallery.albums(),
                        new HeadAndTail<>(payload.filters().value())
                ),
                new HeadAndTail<>(payload.orders().value())
        );

        return Response.ok(albums.loadAll().stream()
                .map(a -> new RestAlbum(a, buildLinks(a)))
                .collect(Collectors.toList())
        );
    }

    private Albums sort(Albums albums, HeadAndTail<AlbumComparator> orders) {
        if (orders.isEmpty()) {
            return albums;
        }

        return new OrderedAlbums(
                sort(albums, orders.tail()),
                orders.head()
        );
    }

    private Albums filter(Albums albums, HeadAndTail<AlbumFilter> filters) {
        if (filters.isEmpty()) {
            return albums;
        }

        return new FilteredAlbums(
                filter(albums, filters.tail()),
                filters.head()
        );
    }

    private List<Link> buildLinks(Album album) {
        return Collections.singletonList(
                new Link(albumUrlTemplate.apply(album.id()), "GET", "album")
        );
    }

    public interface Payload extends ValidatablePayload {
        Maybe<List<AlbumComparator>> orders();

        Maybe<List<AlbumFilter>> filters();
    }

    public static class SerializedPayload implements Payload {
        private final Lazy<Maybe<List<AlbumComparator>>> orders;
        private final Lazy<Maybe<List<AlbumFilter>>> filters;

        public SerializedPayload(Deque<String> orders, Deque<String> filters) {
            this.orders = new Lazy<>(() -> maybe(() -> orders.stream()
                    .map(AlbumComparator::new)
                    .collect(Collectors.toList())));
            this.filters = new Lazy<>(() -> maybe(() -> filters.stream()
                    .map(AlbumFilter.Spec::new)
                    .map(AlbumFilter::new)
                    .collect(Collectors.toList())));
        }

        private <T> Maybe<T> maybe(Supplier<T> throwingSupplier) {
            try {
                return new Maybe.Success<>(throwingSupplier.get());
            } catch (ValidationException e) {
                return new Maybe.Error<>(e.getMessage());
            }
        }

        @Override
        public Maybe<List<AlbumComparator>> orders() {
            return orders.value();
        }

        @Override
        public Maybe<List<AlbumFilter>> filters() {
            return filters.value();
        }

        @Override
        public boolean isValid() {
            return orders().valid() && filters().valid();
        }

        @Override
        public List<String> errors() {
            return Stream.concat(
                    orders().errors().stream(),
                    filters().errors().stream()
            ).collect(Collectors.toList());
        }
    }
}
