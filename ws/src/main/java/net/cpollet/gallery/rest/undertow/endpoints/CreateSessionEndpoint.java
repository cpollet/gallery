package net.cpollet.gallery.rest.undertow.endpoints;

import io.undertow.util.AttachmentKey;
import net.cpollet.gallery.rest.actions.sessions.CreateSession;
import net.cpollet.gallery.rest.core.Action;
import net.cpollet.gallery.rest.core.Response;
import net.cpollet.gallery.rest.core.TransactionalAction;
import net.cpollet.gallery.rest.core.ValidatePayload;
import net.cpollet.gallery.rest.undertow.auth.UndertowUsernamePasswordSessions;
import net.cpollet.gallery.rest.undertow.core.Endpoint;
import net.cpollet.gallery.rest.undertow.core.HttpHeaderActionUrlTemplate;
import net.cpollet.gallery.rest.undertow.core.ValidateEndpoints;
import net.cpollet.gallery.rest.undertow.handlers.RequestWithPayloadHandler;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

public final class CreateSessionEndpoint implements Endpoint {
    private static final AttachmentKey<String> REQUEST_PAYLOAD = AttachmentKey.create(String.class);

    private final TransactionTemplate transactionTemplate;
    private final UndertowUsernamePasswordSessions usernamePasswordSessions;
    private final AttachmentKey<Response> responseAttachmentKey;

    public CreateSessionEndpoint(TransactionTemplate transactionTemplate, UndertowUsernamePasswordSessions usernamePasswordSessions, AttachmentKey<Response> responseAttachmentKey) {
        this.transactionTemplate = transactionTemplate;
        this.usernamePasswordSessions = usernamePasswordSessions;
        this.responseAttachmentKey = responseAttachmentKey;
    }

    @Override
    public void register(ValidateEndpoints handler) {
        handler.post(
                "/sessions",
                new RequestWithPayloadHandler(
                        e -> new TransactionalAction(transactionTemplate, ((Supplier<Action>) () -> {
                            CreateSession.SerializedPayload payload = new CreateSession.SerializedPayload(() -> e.getAttachment(REQUEST_PAYLOAD));
                            return new ValidatePayload(
                                    new CreateSession(
                                            usernamePasswordSessions,
                                            new UndertowUsernamePasswordSessions.UndertowNotifier(e.getSecurityContext()),
                                            payload,
                                            new HttpHeaderActionUrlTemplate(e, "/sessions/%s")
                                    ),
                                    payload
                            );
                        }).get()),
                        REQUEST_PAYLOAD,
                        responseAttachmentKey
                )
        );
    }
}
