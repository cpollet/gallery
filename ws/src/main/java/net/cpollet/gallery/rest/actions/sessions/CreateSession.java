package net.cpollet.gallery.rest.actions.sessions;

import lombok.Getter;
import lombok.Setter;
import net.cpollet.gallery.codec.Deserializer;
import net.cpollet.gallery.codec.JacksonSerialization;
import net.cpollet.gallery.codec.SerializationException;
import net.cpollet.gallery.domain.ValidationException;
import net.cpollet.gallery.domain.logins.Username;
import net.cpollet.gallery.rest.api.request.ValidatablePayload;
import net.cpollet.gallery.rest.api.response.Link;
import net.cpollet.gallery.rest.api.response.RestSession;
import net.cpollet.gallery.rest.auth.AuthCookie;
import net.cpollet.gallery.rest.auth.UsernamePasswordSessions;
import net.cpollet.gallery.rest.core.Action;
import net.cpollet.gallery.rest.core.ActionException;
import net.cpollet.gallery.rest.core.ActionUrlTemplate;
import net.cpollet.gallery.rest.core.Response;
import net.cpollet.gallery.rest.core.UrlEncoded;
import net.cpollet.gallery.rest.undertow.auth.UndertowUsernamePasswordSessions;
import net.cpollet.kozan.lazy.Lazy;
import net.cpollet.kozan.maybe.Maybe;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CreateSession implements Action {
    private final UsernamePasswordSessions usernamePasswordSessions;
    private final Payload payload;
    private final ActionUrlTemplate sessionUrlTemplate;
    private final UsernamePasswordSessions.Notifier notifier;

    public CreateSession(UndertowUsernamePasswordSessions usernamePasswordSessions, UndertowUsernamePasswordSessions.Notifier notifier, Payload payload, ActionUrlTemplate sessionUrlTemplate) {
        this.usernamePasswordSessions = usernamePasswordSessions;
        this.notifier = notifier;
        this.payload = payload;
        this.sessionUrlTemplate = sessionUrlTemplate;
    }

    @Override
    public Response execute() {
        String sessionId = usernamePasswordSessions.create(payload.username().value().toString(), payload.password().value(), notifier)
                .orElseThrow(() -> ActionException.UNAUTHORIZED);

        return Response.created(
                new RestSession(
                        sessionId,
                        buildLinks(sessionId)
                )
        ).withCookie(new AuthCookie(sessionId));
    }

    private List<Link> buildLinks(String session) {
        return Collections.singletonList(
                new Link(sessionUrlTemplate.apply(new UrlEncoded(session)), "DELETE", "logout")
        );
    }

    public interface Payload extends ValidatablePayload {
        Maybe<Username> username();

        Maybe<char[]> password();
    }

    public static class SerializedPayload implements Payload {
        private final Supplier<String> stringSupplier;
        private final Deserializer deserializer;
        private final Lazy<PayloadSpec> payload;
        private final Lazy<Maybe<Username>> username;
        private final Lazy<Maybe<char[]>> password;

        SerializedPayload(Supplier<String> stringSupplier, Deserializer deserializer) {
            this.stringSupplier = stringSupplier;
            this.deserializer = deserializer;
            this.payload = new Lazy<>(this::deserialize);
            this.username = new Lazy<>(() -> maybe(() -> new Username(payload.value().username)));
            this.password = new Lazy<>(() -> maybe(() -> payload.value().password));
        }

        public SerializedPayload(Supplier<String> stringSupplier) {
            this(stringSupplier, new JacksonSerialization());
        }

        private CreateSession.SerializedPayload.PayloadSpec deserialize() {
            try {
                return deserializer.deserialize(stringSupplier.get(), CreateSession.SerializedPayload.PayloadSpec.class);
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
        public Maybe<Username> username() {
            return username.value();
        }

        @Override
        public Maybe<char[]> password() {
            return password.value();
        }

        @Override
        public boolean isValid() {
            return username().valid() && password().valid();
        }

        @Override
        public List<String> errors() {
            return Stream.concat(
                    username().errors().stream(),
                    password().errors().stream()
            ).collect(Collectors.toList());
        }

        @Getter
        @Setter
        private static class PayloadSpec {
            private String username;
            private char[] password;
        }
    }
}
