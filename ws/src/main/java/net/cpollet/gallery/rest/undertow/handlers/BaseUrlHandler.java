package net.cpollet.gallery.rest.undertow.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

public final class BaseUrlHandler implements HttpHandler {
    public static final HttpString BASE_URL_HEADER = HttpString.tryFromString("X-Gallery-BaseUrl");

    private final HttpHandler wrapped;

    public BaseUrlHandler(HttpHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (!exchange.getRequestHeaders().contains(BASE_URL_HEADER)) {
            exchange.getRequestHeaders().add(BASE_URL_HEADER, exchange.getRequestScheme() + "://" + exchange.getHostAndPort());
        }

        wrapped.handleRequest(exchange);
    }
}
