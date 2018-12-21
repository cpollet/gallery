package net.cpollet.gallery.rest.undertow.auth;

import io.undertow.security.idm.Account;
import io.undertow.security.idm.Credential;
import io.undertow.security.idm.IdentityManager;

public final class FailingIdentityManager implements IdentityManager {
    @Override
    public Account verify(Account account) {
        throw oneShouldNeverComeHere();
    }

    private IllegalStateException oneShouldNeverComeHere() {
        return new IllegalStateException("Server is not well configured, one should never depend on this IdentityManager.");
    }

    @Override
    public Account verify(String id, Credential credential) {
        throw oneShouldNeverComeHere();
    }

    @Override
    public Account verify(Credential credential) {
        throw oneShouldNeverComeHere();
    }
}
