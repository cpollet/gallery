package net.cpollet.gallery.rest.core.cookie;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DomainTest {
    @Test
    void isSet_returnsFalse_whenDomainIsNull() {
        // GIVEN
        Domain domain = Domain.domain(null);

        // WHEN
        boolean isSet = domain.isSet();

        // THEN
        Assertions.assertFalse(isSet);
    }

    @Test
    void isSet_returnsFalse_whenDomainIsEmpty() {
        // GIVEN
        Domain domain = Domain.domain("");

        // WHEN
        boolean isSet = domain.isSet();

        // THEN
        Assertions.assertFalse(isSet);
    }

    @Test
    void isSet_returnsFalse_whenDomainIsSame() {
        // GIVEN
        Domain domain = Domain.same();

        // WHEN
        boolean isSet = domain.isSet();

        // THEN
        Assertions.assertFalse(isSet);
    }

    @Test
    void isSet_returnsTrue_whenDomainIsNotEmpty() {
        // GIVEN
        Domain domain = Domain.domain("domain");

        // WHEN
        boolean isSet = domain.isSet();

        // THEN
        Assertions.assertTrue(isSet);
    }

    @Test
    void toString_throwsIllegalStateException_whenDomainIsNotSet() {
        // GIVEN
        Domain domain = Domain.domain("");

        // WHEN + THEN
        try {
            domain.toString();
            Assertions.fail("Exception not throws");
        } catch (IllegalStateException e) {
            // success
        }
    }

    @Test
    void toString_returnsDomain_whenDomainIsSet() {
        // GIVEN
        Domain domain = Domain.domain("domain");

        // WHEN
        String domainStr = domain.toString();

        // THEN
        Assertions.assertEquals("domain", domainStr);
    }
}