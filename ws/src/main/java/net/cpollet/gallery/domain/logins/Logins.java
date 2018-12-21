package net.cpollet.gallery.domain.logins;

import java.util.Optional;

public interface Logins {
    Optional<CachedLogin> loadByUsername(Username username);
}
