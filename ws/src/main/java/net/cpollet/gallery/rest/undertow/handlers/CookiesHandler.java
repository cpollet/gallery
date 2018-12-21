package net.cpollet.gallery.rest.undertow.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.CookieImpl;
import net.cpollet.gallery.rest.core.Response;
import net.cpollet.gallery.rest.core.cookie.Confidentiality;
import net.cpollet.gallery.rest.core.cookie.Domain;
import net.cpollet.gallery.rest.core.cookie.Expiration;
import net.cpollet.gallery.rest.core.cookie.MaxAge;
import net.cpollet.gallery.rest.core.cookie.Origin;
import net.cpollet.gallery.rest.core.cookie.Path;
import net.cpollet.gallery.rest.core.cookie.Scope;
import net.cpollet.gallery.rest.undertow.UndertowServer;

import java.util.Optional;

public final class CookiesHandler implements HttpHandler {
    private final HttpHandler wrapped;

    public CookiesHandler(HttpHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        wrapped.handleRequest(exchange);

        Response response = Optional
                .ofNullable(exchange.getAttachment(UndertowServer.RESPONSE))
                .orElse(Response.noContent());

        response.mapCookies(c ->
                c.accept((name, value, expiration, maxAge, domain, path, confidentiality, scope, origin) ->
                        path(path,
                                domain(domain,
                                        maxAge(maxAge,
                                                expires(expiration,
                                                        new CookieImpl(name, value)
                                                                .setSecure(confidentiality == Confidentiality.SECURE)
                                                                .setHttpOnly(scope == Scope.HTTP_ONLY)
                                                                .setSameSite(origin == Origin.SAME_SITE)
                                                )
                                        )
                                )
                        )

                )
        ).forEach(exchange::setResponseCookie);
    }

    private Cookie path(Path path, Cookie cookie) {
        if (!path.isSet()) {
            return cookie;
        }
        return cookie.setPath(path.toString());
    }

    private Cookie domain(Domain domain, Cookie cookie) {
        if (!domain.isSet()) {
            return cookie;
        }
        return cookie.setDomain(domain.toString());
    }

    private Cookie maxAge(MaxAge maxAge, Cookie cookie) {
        if (!maxAge.isSet()) {
            return cookie;
        }
        return cookie.setMaxAge(maxAge.toSeconds());
    }

    private Cookie expires(Expiration expiration, Cookie cookie) {
        if (!expiration.isSet()) {
            return cookie;
        }
        return cookie.setExpires(expiration.toDate());
    }
}
