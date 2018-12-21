package net.cpollet.gallery.domain.logins;

import com.google.common.collect.ImmutableMap;
import net.cpollet.gallery.database.Database;

import java.util.Optional;

public final class PgLogins implements Logins {
    private final Database database;

    public PgLogins(Database database) {
        this.database = database;
    }

    @Override
    public Optional<CachedLogin> loadByUsername(Username username) {
        return database.query("SELECT id, username FROM logins WHERE username=:username")
                .with(ImmutableMap.of(
                        "username", username.toString()
                ))
                .fetch((rs, rowNum) -> new CachedLogin(
                        new PgLogin(
                                database,
                                rs.getLong("id")
                        )
                ))
                .stream()
                .findFirst();
    }
}
