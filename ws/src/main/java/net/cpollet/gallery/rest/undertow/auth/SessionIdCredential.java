package net.cpollet.gallery.rest.undertow.auth;

import io.undertow.security.idm.Credential;

public final class SessionIdCredential implements Credential {
    private final String sessionId;

    public SessionIdCredential(String sessionId) {
        this.sessionId = sessionId;
    }

    public String sessionId() {
        return sessionId;
    }
}
