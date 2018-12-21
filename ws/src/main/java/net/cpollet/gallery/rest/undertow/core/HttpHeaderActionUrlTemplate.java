package net.cpollet.gallery.rest.undertow.core;

import io.undertow.server.HttpServerExchange;
import net.cpollet.gallery.rest.core.ActionUrlTemplate;
import net.cpollet.gallery.rest.undertow.handlers.BaseUrlHandler;

public final class HttpHeaderActionUrlTemplate implements ActionUrlTemplate {
    private final HttpServerExchange exchange;
    private final String pathTemplate;

    public HttpHeaderActionUrlTemplate(HttpServerExchange exchange, String pathTemplate) {
        this.exchange = exchange;
        this.pathTemplate = pathTemplate;
    }

    @Override
    public String apply(Object... objects) {
        return String.format(urlTemplate(), objects);
    }

    private String urlTemplate() {
        return baseUrl() + pathTemplate;
    }

    private String baseUrl() {
        return exchange.getRequestHeaders().get(BaseUrlHandler.BASE_URL_HEADER).getFirst();
    }
}
