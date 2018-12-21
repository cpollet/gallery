package net.cpollet.gallery.rest.core.cookie;

import java.util.Date;

public final class Expiration {
    private static final Expiration NO_EXPIRATION = new Expiration();

    private final Date expirationDate;

    private Expiration(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    private Expiration() {
        this(null);
    }

    public static Expiration noExpiration() {
        return NO_EXPIRATION;
    }

    public static Expiration expiresOn(Date date) {
        return new Expiration(date);
    }

    public boolean isSet() {
        return expirationDate != null;
    }

    public Date toDate() {
        if (!isSet()) {
            throw new IllegalStateException("No expiration configured");
        }

        return expirationDate;
    }
}
