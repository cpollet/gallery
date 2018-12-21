package net.cpollet.gallery.rest.core.cookie;

import java.util.concurrent.TimeUnit;

public final class MaxAge {
    private static final MaxAge NO_MAX_AGE = new MaxAge();
    private static final MaxAge EXPIRED = new MaxAge(0);

    private final Integer quantity;
    private final TimeUnit timeUnit;

    private MaxAge(Integer quantity, TimeUnit unit) {
        this.quantity = quantity;
        this.timeUnit = unit;
    }

    private MaxAge(Integer seconds) {
        this(seconds, TimeUnit.SECONDS);
    }

    private MaxAge() {
        this(null);
    }

    public static MaxAge noMaxAge() {
        return NO_MAX_AGE;
    }

    public static MaxAge expired() {
        return EXPIRED;
    }

    public static MaxAge expiresIn(int quantity, TimeUnit unit) {
        return new MaxAge(quantity, unit);
    }

    public boolean isSet() {
        return quantity != null;
    }

    public int toSeconds() {
        if (!isSet()) {
            throw new IllegalStateException("No max age configured");
        }
        return (int) TimeUnit.SECONDS.convert(quantity, timeUnit);
    }
}
