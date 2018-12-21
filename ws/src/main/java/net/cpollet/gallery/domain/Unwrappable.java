package net.cpollet.gallery.domain;

public interface Unwrappable<B> {
    <T extends B> T unwrap(Class<T> clazz);
}
