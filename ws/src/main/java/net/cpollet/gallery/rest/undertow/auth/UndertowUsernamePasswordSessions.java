package net.cpollet.gallery.rest.undertow.auth;

import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.SecurityContext;
import io.undertow.security.idm.Account;
import io.undertow.security.idm.PasswordCredential;
import io.undertow.security.impl.BasicAuthenticationMechanism;
import net.cpollet.gallery.domain.logins.Logins;
import net.cpollet.gallery.rest.auth.AuthCookie;
import net.cpollet.gallery.rest.auth.Sessions;
import net.cpollet.gallery.rest.auth.UsernamePasswordSessions;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class UndertowUsernamePasswordSessions implements UsernamePasswordSessions {
    private final Sessions sessions;
    private final CookieMechanism cookieMechanism;
    private final BasicAuthenticationMechanism basicAuthMechanism;
    private final UsernamePasswordIdentityManager usernamePasswordIdentityManager;

    public UndertowUsernamePasswordSessions(Logins logins, Sessions sessions) {
        this.sessions = sessions;
        this.usernamePasswordIdentityManager = new UsernamePasswordIdentityManager(logins);
        this.cookieMechanism = new CookieMechanism(
                new AuthCookie(""),
                new SessionIdIdentityManager(sessions)
        );
        this.basicAuthMechanism = new BasicAuthenticationMechanism(
                "api",
                "BASIC",
                false,
                usernamePasswordIdentityManager
        );
    }

    public List<AuthenticationMechanism> authenticationMechanisms() {
        return Arrays.asList(
                cookieMechanism,
                basicAuthMechanism
        );
    }

    public Optional<String> create(String username, char[] password, UsernamePasswordSessions.Notifier notifier) {
        Account account = usernamePasswordIdentityManager.verify(username, new PasswordCredential(password));

        if (account == null) {
            return Optional.empty();
        }

        String sessionId = sessions.create(username);

        notifier.notifySessionCreated(account);

        return Optional.of(sessionId);
    }

    public static class UndertowNotifier implements UsernamePasswordSessions.Notifier {
        private final SecurityContext context;

        public UndertowNotifier(SecurityContext context) {
            this.context = context;
        }

        @Override
        public void notifySessionCreated(Object object) {
            if (!(object instanceof Account)) {
                throw new IllegalArgumentException(String.format("Expected to have an instance of Account, got %s", object.getClass().getName()));
            }

            authenticationComplete((Account) object);
        }

        private void authenticationComplete(Account account) {
            context.authenticationComplete(account, "SESSION", false);
        }
    }
}
