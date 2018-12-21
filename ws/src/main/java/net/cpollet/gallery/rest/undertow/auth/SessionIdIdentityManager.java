package net.cpollet.gallery.rest.undertow.auth;

import io.undertow.security.idm.Account;
import io.undertow.security.idm.Credential;
import io.undertow.security.idm.IdentityManager;
import net.cpollet.gallery.rest.auth.Sessions;

public final class SessionIdIdentityManager implements IdentityManager {
    private final Sessions sessions;

    public SessionIdIdentityManager(Sessions sessions) {
        this.sessions = sessions;
    }

    @Override
    public Account verify(Account account) {
        throw new IllegalStateException();
    }

    @Override
    public Account verify(String id, Credential credential) {
        throw new IllegalStateException();
    }

    @Override
    public Account verify(Credential credential) {
        if (!(credential instanceof SessionIdCredential)) {
            throw new IllegalArgumentException(String.format("Expected to have an instance of SessionIdCredential, got %s", credential.getClass().getName()));
        }

        return verify((SessionIdCredential) credential);
    }

    private Account verify(SessionIdCredential credential) {
        return sessions.load(credential.sessionId())
                .map(u -> new DefaultAccount(new UsernamePrincipal(u)))
                .orElse(null);
    }
}
