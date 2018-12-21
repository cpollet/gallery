package net.cpollet.gallery.rest.core.cookie;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PathTest {
    @Test
    void isSet_returnsFalse_whenPathIsNull() {
        // GIVEN
        Path path = Path.path(null);

        // WHEN
        boolean isSet = path.isSet();

        // THEN
        Assertions.assertFalse(isSet);
    }

    @Test
    void isSet_returnsFalse_whenPathIsEmpty() {
        // GIVEN
        Path path = Path.path("");

        // WHEN
        boolean isSet = path.isSet();

        // THEN
        Assertions.assertFalse(isSet);
    }

    @Test
    void isSet_returnsFalse_whenPathIsSame() {
        // GIVEN
        Path path = Path.same();

        // WHEN
        boolean isSet = path.isSet();

        // THEN
        Assertions.assertFalse(isSet);
    }

    @Test
    void isSet_returnsTrue_whenPathIsRoot() {
        // GIVEN
        Path path = Path.root();

        // WHEN
        boolean isSet = path.isSet();

        // THEN
        Assertions.assertTrue(isSet);
    }

    @Test
    void isSet_returnsTrue_whenPathIsNotEmpty() {
        // GIVEN
        Path path = Path.path("path");

        // WHEN
        boolean isSet = path.isSet();

        // THEN
        Assertions.assertTrue(isSet);
    }

    @Test
    void toString_throwsIllegalStateException_whenPathIsNotSet() {
        // GIVEN
        Path path = Path.path("");

        // WHEN + THEN
        try {
            path.toString();
            Assertions.fail("Exception not throws");
        } catch (IllegalStateException e) {
            // success
        }
    }

    @Test
    void toString_returnsPath_whenPathIsSet() {
        // GIVEN
        Path path = Path.path("path");

        // WHEN
        String pathStr = path.toString();

        // THEN
        Assertions.assertEquals("path", pathStr);
    }
}