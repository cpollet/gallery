package net.cpollet.gallery.rest.api.request;

import java.util.List;

public interface ValidatablePayload {
    boolean isValid();

    List<String> errors();
}
