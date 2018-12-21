package net.cpollet.gallery.rest.auth;

import java.util.Optional;

public interface Sessions {
    String create(String username);

    Optional<String> load(String sessionId);
}
