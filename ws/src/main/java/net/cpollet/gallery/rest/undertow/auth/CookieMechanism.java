package net.cpollet.gallery.rest.undertow.auth;

import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.SecurityContext;
import io.undertow.security.idm.Account;
import io.undertow.security.idm.IdentityManager;
import io.undertow.server.HttpServerExchange;
import net.cpollet.gallery.rest.core.cookie.Cookie;
import net.cpollet.kozan.collections.Pair;

public final class CookieMechanism implements AuthenticationMechanism {
    private final Cookie cookie;
    private final IdentityManager identityManager;

    public CookieMechanism(Cookie authCookie, IdentityManager identityManager) {
        this.cookie = authCookie;
        this.identityManager = identityManager;
    }

    @Override
    public AuthenticationMechanismOutcome authenticate(HttpServerExchange exchange, SecurityContext securityContext) {
        return exchange.getRequestCookies().entrySet().stream()
                .map(e -> Pair.of(e.getKey(), e.getValue().getValue()))
                .filter(pair -> cookie.accept((name, v, e, m, d, p, c, s, o) -> name.equals(pair.getKey())))
                .map(Pair::getValue)
                .findFirst()
                .map(s -> validateSession(s, securityContext))
                .orElse(AuthenticationMechanismOutcome.NOT_ATTEMPTED);
    }

    private AuthenticationMechanismOutcome validateSession(String sessionId, SecurityContext securityContext) {
        Account account = identityManager.verify(
                new SessionIdCredential(sessionId)
        );

        if (account == null) {
            return AuthenticationMechanismOutcome.NOT_ATTEMPTED;
        }

        securityContext.authenticationComplete(account, "COOKIE", false);

        return AuthenticationMechanismOutcome.AUTHENTICATED;
    }


    @Override
    public ChallengeResult sendChallenge(HttpServerExchange exchange, SecurityContext securityContext) {
        return ChallengeResult.NOT_SENT;
    }
}
