package net.cpollet.gallery.rest.undertow.auth;

import io.undertow.security.idm.Account;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

public final class DefaultAccount implements Account {
    private static final long serialVersionUID = 982364876234324L;

    private final UsernamePrincipal principal;

    DefaultAccount(UsernamePrincipal principal) {
        this.principal = principal;
    }

    @Override
    public Principal getPrincipal() {
        return principal;
    }

    @Override
    public Set<String> getRoles() {
        return Collections.emptySet();
    }
}
