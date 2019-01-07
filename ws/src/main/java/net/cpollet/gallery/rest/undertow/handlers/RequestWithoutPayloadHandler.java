package net.cpollet.gallery.rest.undertow.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.AttachmentKey;
import net.cpollet.gallery.rest.core.Action;
import net.cpollet.gallery.rest.core.Response;

import java.util.function.Function;

public final class RequestWithoutPayloadHandler implements HttpHandler {
    private final Function<HttpServerExchange, Action> action;
    private final AttachmentKey<Response> responseAttachmentKey;

    public RequestWithoutPayloadHandler(Function<HttpServerExchange, Action> action, AttachmentKey<Response> responseAttachmentKey) {
        this.action = action;
        this.responseAttachmentKey = responseAttachmentKey;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        exchange.putAttachment(
                responseAttachmentKey,
                action.apply(exchange).execute()
        );
    }
}
