package net.cpollet.gallery.rest.undertow.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import net.cpollet.gallery.codec.JacksonSerialization;
import net.cpollet.gallery.codec.Serializer;
import net.cpollet.gallery.rest.core.Response;
import net.cpollet.gallery.rest.undertow.UndertowServer;

import java.util.Optional;

public final class SerializeHandler implements HttpHandler {
    private final HttpHandler next;
    private final Serializer serializer;

    public SerializeHandler(HttpHandler next, Serializer serializer) {
        this.next = next;
        this.serializer = serializer;
    }

    public SerializeHandler(HttpHandler next) {
        this(next, new JacksonSerialization());
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }

        next.handleRequest(exchange);

        if (!exchange.isComplete()) {
            Response response = Optional
                    .ofNullable(exchange.getAttachment(UndertowServer.RESPONSE))
                    .orElse(Response.noContent());

            exchange.setStatusCode(response.code());

            exchange.getResponseSender().send(
                    serializer.serialize(response.payload())
            );
        }
    }
}
