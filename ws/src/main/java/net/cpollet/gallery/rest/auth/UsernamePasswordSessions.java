package net.cpollet.gallery.rest.auth;

import java.util.Optional;

public interface UsernamePasswordSessions {
    Optional<String> create(String username, char[] password, UsernamePasswordSessions.Notifier notifier);

    interface Notifier {
        void notifySessionCreated(Object object);
    }
}
