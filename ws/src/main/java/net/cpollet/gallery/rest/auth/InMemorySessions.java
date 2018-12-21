package net.cpollet.gallery.rest.auth;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemorySessions implements Sessions {
    private final Map<String, String> sessions;

    public InMemorySessions() {
        this.sessions = new ConcurrentHashMap<>();
    }

    @Override
    public String create(String username) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, username);
        return sessionId;
    }

    @Override
    public Optional<String> load(String sessionId) {
        if (!sessions.containsKey(sessionId)) {
            return Optional.empty();
        }
        return Optional.of(sessions.get(sessionId));
    }
}
