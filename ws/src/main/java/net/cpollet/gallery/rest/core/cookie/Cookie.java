package net.cpollet.gallery.rest.core.cookie;

public interface Cookie {
    <T> T accept(Visitor<T> visitor);
}
