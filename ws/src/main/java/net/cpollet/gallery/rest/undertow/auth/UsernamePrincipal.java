package net.cpollet.gallery.rest.undertow.auth;

import java.io.Serializable;
import java.security.Principal;

public final class UsernamePrincipal implements Principal, Serializable {
    private static final long serialVersionUID = 8344456654546785323L;

    private final String username;

    UsernamePrincipal(String username) {
        this.username = username;
    }

    @Override
    public String getName() {
        return username;
    }
}
