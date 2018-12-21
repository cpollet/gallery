package net.cpollet.gallery.rest.undertow.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import net.cpollet.gallery.logging.CorrelationIdProvider;
import net.cpollet.gallery.logging.MDCCorrelationIdProvider;

public final class CorrelationIdHandler implements HttpHandler {
    public static final HttpString CORRELATION_ID_HEADER = HttpString.tryFromString("X-Gallery-CorrelationId");

    private final HttpHandler next;
    private final CorrelationIdProvider correlationIdProvider;

    public CorrelationIdHandler(HttpHandler next, CorrelationIdProvider correlationIdProvider) {
        this.next = next;
        this.correlationIdProvider = correlationIdProvider;
    }

    public CorrelationIdHandler(HttpHandler next) {
        this(next, new MDCCorrelationIdProvider());
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String correlationId = correlationId(exchange);

        exchange.getRequestHeaders().add(CORRELATION_ID_HEADER, correlationId);
        exchange.getResponseHeaders().add(CORRELATION_ID_HEADER, correlationId);

        next.handleRequest(exchange);
    }

    private String correlationId(HttpServerExchange exchange) {
        if (exchange.getRequestHeaders().contains(CORRELATION_ID_HEADER)) {
            return exchange.getRequestHeaders().get(CORRELATION_ID_HEADER).getFirst();
        }

        return correlationIdProvider.generate();
    }
}
