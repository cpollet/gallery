package net.cpollet.gallery.rest.core.cookie;

public interface Visitor<T> {
    @SuppressWarnings("squid:S00107")
    T visit(
            String name,
            String value,
            Expiration expiration,
            MaxAge maxAge,
            Domain domain,
            Path path,
            Confidentiality confidentiality,
            Scope scope,
            Origin origin
    );
}
