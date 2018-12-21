package net.cpollet.gallery.domain.logins;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PasswordTest {
     @Test
    void matches_returnsTrue_whenPasswordMatches(){
         // GIVEN
         String hashedPassword = new Password("clear".toCharArray()).toString();

         // WHEN
         boolean matches = new Password(hashedPassword).matches("clear".toCharArray());

         // THEN
         Assertions.assertTrue(matches);
     }

    @Test
    void matches_returnsFalse_whenPasswordDoesNotMatch(){
        // GIVEN
        String hashedPassword = new Password("clear".toCharArray()).toString();

        // WHEN
        boolean matches = new Password(hashedPassword).matches("clear2".toCharArray());

        // THEN
        Assertions.assertFalse(matches);
    }
}
