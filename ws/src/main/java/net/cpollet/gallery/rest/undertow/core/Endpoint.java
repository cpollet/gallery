package net.cpollet.gallery.rest.undertow.core;

public interface Endpoint {
    void register(ValidateEndpoints handler);
}
