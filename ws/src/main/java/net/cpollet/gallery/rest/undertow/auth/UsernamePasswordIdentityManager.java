package net.cpollet.gallery.rest.undertow.auth;

import io.undertow.security.idm.Account;
import io.undertow.security.idm.Credential;
import io.undertow.security.idm.IdentityManager;
import io.undertow.security.idm.PasswordCredential;
import net.cpollet.gallery.domain.logins.CachedLogin;
import net.cpollet.gallery.domain.logins.Logins;
import net.cpollet.gallery.domain.logins.Username;

import java.util.Optional;

public final class UsernamePasswordIdentityManager implements IdentityManager {
    private final Logins logins;

    public UsernamePasswordIdentityManager(Logins logins) {
        this.logins = logins;
    }

    @Override
    public Account verify(Account account) {
        throw new IllegalStateException();
    }

    @Override
    public Account verify(String id, Credential credential) {
        if (!(credential instanceof PasswordCredential)) {
            throw new IllegalArgumentException(String.format("Expected to have an instance of PasswordCredential, got %s", credential.getClass().getName()));
        }

        return verify(id, (PasswordCredential) credential);
    }

    private Account verify(String id, PasswordCredential credential) {
        Optional<CachedLogin> login = logins.loadByUsername(new Username(id));

        if (!login.isPresent()) {
            return null;
        }

        if (!login.get().passwordMatches(credential.getPassword())) {
            return null;
        }

        return new DefaultAccount(new UsernamePrincipal(id));
    }

    @Override
    public Account verify(Credential credential) {
        throw new IllegalStateException();
    }
}
