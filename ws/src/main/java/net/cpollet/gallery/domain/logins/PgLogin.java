package net.cpollet.gallery.domain.logins;

import net.cpollet.gallery.database.Database;
import net.cpollet.gallery.domain.logins.exceptions.LoginNotFoundException;

import java.util.Map;

public final class PgLogin implements Login {
    private final Database database;
    private final long id;

    public PgLogin(Database database, long id) {
        this.database = database;
        this.id = id;
    }

    @Override
    public boolean passwordMatches(char[] password) {
        return new Password(hashedPassword()).matches(password);
    }

    private String hashedPassword() {
        return database.query("SELECT password FROM logins WHERE id=:id")
                .with(Map.of(
                        "id", id
                ))
                .fetch((rs, rowNum) -> rs.getString("password"))
                .stream()
                .findFirst()
                .orElseThrow(() -> new LoginNotFoundException(id));
    }
}
