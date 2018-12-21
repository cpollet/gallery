package net.cpollet.gallery.rest.auth;

import net.cpollet.gallery.rest.core.cookie.Confidentiality;
import net.cpollet.gallery.rest.core.cookie.Cookie;
import net.cpollet.gallery.rest.core.cookie.Domain;
import net.cpollet.gallery.rest.core.cookie.Expiration;
import net.cpollet.gallery.rest.core.cookie.MaxAge;
import net.cpollet.gallery.rest.core.cookie.Origin;
import net.cpollet.gallery.rest.core.cookie.Path;
import net.cpollet.gallery.rest.core.cookie.Scope;
import net.cpollet.gallery.rest.core.cookie.Visitor;

public final class AuthCookie implements Cookie {
    private final String value;

    public AuthCookie(String sessionId) {
        this.value = sessionId;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(
                "big42",
                value,
                Expiration.noExpiration(),
                MaxAge.noMaxAge(),
                Domain.same(),
                Path.root(),
                Confidentiality.SECURE,
                Scope.HTTP_ONLY,
                Origin.SAME_SITE
        );
    }
}
