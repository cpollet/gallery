package net.cpollet.gallery.rest.core.cookie;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

class MaxAgeTest {
    @Test
    void isSet_returnsFalse_whenNoMaxAge() {
        // GIVEN
        MaxAge maxAge = MaxAge.noMaxAge();

        // WHEN
        boolean isSet = maxAge.isSet();

        // THEN
        Assertions.assertFalse(isSet);
    }

    @Test
    void isSet_returnsTrue_whenExpired() {
        // GIVEN
        MaxAge maxAge = MaxAge.expired();

        // WHEN
        boolean isSet = maxAge.isSet();

        // THEN
        Assertions.assertTrue(isSet);
    }

    @Test
    void isSet_returnsTrue_whenEventuallyExpires() {
        // GIVEN
        MaxAge maxAge = MaxAge.expiresIn(1, TimeUnit.SECONDS);

        // WHEN
        boolean isSet = maxAge.isSet();

        // THEN
        Assertions.assertTrue(isSet);
    }

    @Test
    void toSeconds_throwsException_whenMaxAgeNotSet() {
        // GIVEN
        MaxAge maxAge = MaxAge.noMaxAge();

        // WHEN + THEN
        try {
            maxAge.toSeconds();
            Assertions.fail("Exception not thrown");
        } catch (IllegalStateException e) {
            // success
        }
    }

    @Test
    void toSeconds_returnsValueInSeconds_whenMaxAgeSet() {
        // GIVEN
        MaxAge maxAge = MaxAge.expiresIn(1, TimeUnit.MINUTES);

        // WHEN
        int seconds = maxAge.toSeconds();

        // THEN
        Assertions.assertEquals(60, seconds);
    }
}