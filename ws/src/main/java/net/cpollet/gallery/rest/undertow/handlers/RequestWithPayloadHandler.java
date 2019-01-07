package net.cpollet.gallery.rest.undertow.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.AttachmentKey;
import net.cpollet.gallery.rest.core.Action;
import net.cpollet.gallery.rest.core.Response;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public final class RequestWithPayloadHandler implements HttpHandler {
    private final Function<HttpServerExchange, Action> action;
    private final AttachmentKey<String> requestAttachmentKey;
    private final AttachmentKey<Response> responseAttachmentKey;

    public RequestWithPayloadHandler(Function<HttpServerExchange, Action> action, AttachmentKey<String> requestAttachmentKey, AttachmentKey<Response> responseAttachmentKey) {
        this.action = action;
        this.requestAttachmentKey = requestAttachmentKey;
        this.responseAttachmentKey = responseAttachmentKey;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        exchange.getRequestReceiver().receiveFullBytes(this::getFullBytesCallback);
    }

    private void getFullBytesCallback(HttpServerExchange exchange, byte[] data) {
        exchange.putAttachment(requestAttachmentKey, new String(data, 0, data.length, StandardCharsets.UTF_8));
        exchange.putAttachment(
                responseAttachmentKey,
                action.apply(exchange).execute()
        );
    }
}
