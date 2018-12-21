package net.cpollet.gallery.rest.core.cookie;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

class ExpirationTest {
    @Test
    void isSet_returnsFalse_whenDateIsNull() {
        // GIVEN
        Expiration expiration = Expiration.expiresOn(null);

        // WHEN
        boolean isSet = expiration.isSet();

        // THEN
        Assertions.assertFalse(isSet);
    }

    @Test
    void isSet_returnsFalse_whenNoExpiration() {
        // GIVEN
        Expiration expiration = Expiration.noExpiration();

        // WHEN
        boolean isSet = expiration.isSet();

        // THEN
        Assertions.assertFalse(isSet);
    }

    @Test
    void isSet_returnsTrue_whenExpirationIsSet() {
        // GIVEN
        Expiration expiration = Expiration.expiresOn(new Date());

        // WHEN
        boolean isSet = expiration.isSet();

        // THEN
        Assertions.assertTrue(isSet);
    }


    @Test
    void toString_throwsIllegalStateException_whenExpirationDateIsNotSet() {
        // GIVEN
        Expiration expiration = Expiration.noExpiration();

        // WHEN + THEN
        try {
            expiration.toDate();
            Assertions.fail("Exception not throws");
        } catch (IllegalStateException e) {
            // success
        }
    }

    @Test
    void toString_returnExpirationDate_whenExpirationDateIsSet() {
        // GIVEN
        Date expectedDate = new Date();
        Expiration expiration = Expiration.expiresOn(expectedDate);

        // WHEN
        Date date = expiration.toDate();

        // THEN
        Assertions.assertEquals(expectedDate, date);
    }

}