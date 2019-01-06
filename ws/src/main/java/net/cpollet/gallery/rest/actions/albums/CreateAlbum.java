package net.cpollet.gallery.rest.actions.albums;

import net.cpollet.gallery.codec.Deserializer;
import net.cpollet.gallery.codec.JacksonSerialization;
import net.cpollet.gallery.codec.SerializationException;
import net.cpollet.gallery.domain.ValidationException;
import net.cpollet.gallery.domain.albums.Album;
import net.cpollet.gallery.domain.albums.AlbumDescription;
import net.cpollet.gallery.domain.albums.AlbumName;
import net.cpollet.gallery.domain.albums.ObservableAlbum;
import net.cpollet.gallery.domain.albums.events.AlbumEventsListenerTemplate;
import net.cpollet.gallery.domain.gallery.Gallery;
import net.cpollet.gallery.rest.api.request.ValidatablePayload;
import net.cpollet.gallery.rest.api.response.Link;
import net.cpollet.gallery.rest.api.response.RestAlbum;
import net.cpollet.gallery.rest.core.Action;
import net.cpollet.gallery.rest.core.ActionException;
import net.cpollet.gallery.rest.core.ActionUrlTemplate;
import net.cpollet.gallery.rest.core.Response;
import net.cpollet.kozan.lazy.Lazy;
import net.cpollet.kozan.maybe.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CreateAlbum implements Action {
    private final Logger log = LoggerFactory.getLogger(CreateAlbum.class);

    private final Gallery gallery;
    private final Payload payload;
    private final ActionUrlTemplate albumUrlTemplate;

    public CreateAlbum(Gallery gallery, Payload payload, ActionUrlTemplate albumUrlTemplate) {
        this.gallery = gallery;
        this.payload = payload;
        this.albumUrlTemplate = albumUrlTemplate;
    }

    @Override
    public Response execute() {
        Album album = new ObservableAlbum(
                gallery.albums().create(payload.name().value()),
                new AlbumEventsListenerTemplate() {
                    @Override
                    public void tagsUpdated(Album album, List<String> oldTags) {
                        gallery.tags().tagsOnAlbum(album.id(), album.tags());
                    }
                })
                .description(payload.description().value());

        return Response.created(new RestAlbum(album, buildLinks(album)));
    }

    private List<Link> buildLinks(Album album) {
        return Collections.singletonList(
                new Link(albumUrlTemplate.apply(album.id()), "GET", "album")
        );
    }

    public interface Payload extends ValidatablePayload {
        Maybe<AlbumName> name();

        Maybe<AlbumDescription> description();
    }

    public static class SerializedPayload implements Payload {
        private final Deserializer deserializer;
        private final Supplier<String> stringSupplier;
        private final Lazy<PayloadSpec> payload;
        private final Lazy<Maybe<AlbumName>> name;
        private final Lazy<Maybe<AlbumDescription>> description;

        public SerializedPayload(Supplier<String> stringSupplier, Deserializer deserializer) {
            this.stringSupplier = stringSupplier;
            this.deserializer = deserializer;
            this.payload = new Lazy<>(this::deserialize);
            this.name = new Lazy<>(() -> maybe(() -> new AlbumName(payload.value().name)));
            this.description = new Lazy<>(() -> maybe(() -> new AlbumDescription(payload.value().description)));
        }

        public SerializedPayload(Supplier<String> stringSupplier) {
            this(stringSupplier, new JacksonSerialization());
        }

        public SerializedPayload(String string, Deserializer deserializer) {
            this(() -> string, deserializer);
        }

        public SerializedPayload(String string) {
            this(() -> string);
        }

        private SerializedPayload.PayloadSpec deserialize() {
            try {
                return deserializer.deserialize(stringSupplier.get(), SerializedPayload.PayloadSpec.class);
            } catch (SerializationException e) {
                throw ActionException.PAYLOAD_NOT_DESERIALIZABLE;
            }
        }

        private <T> Maybe<T> maybe(Supplier<T> throwingSupplier) {
            try {
                return new Maybe.Success<>(throwingSupplier.get());
            } catch (ValidationException e) {
                return new Maybe.Error<>(e.getMessage());
            }
        }

        @Override
        public Maybe<AlbumName> name() {
            return name.value();
        }

        @Override
        public Maybe<AlbumDescription> description() {
            return description.value();
        }

        @Override
        public boolean isValid() {
            return name().valid() && description().valid();
        }

        @Override
        public List<String> errors() {
            return Stream.concat(
                    name().errors().stream(),
                    description().errors().stream()
            ).collect(Collectors.toList());
        }

        private static class PayloadSpec {
            private String name;
            private String description;

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
        }
    }
}
