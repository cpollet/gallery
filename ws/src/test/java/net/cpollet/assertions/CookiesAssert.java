package net.cpollet.assertions;

import org.junit.jupiter.api.Assertions;

import java.net.HttpCookie;
import java.util.List;
import java.util.stream.Collectors;

public final class CookiesAssert {
    private final List<HttpCookie> cookies;

    CookiesAssert(List<String> cookies) {
        this.cookies = cookies.stream()
                .map(c -> HttpCookie.parse(c).get(0))
                .collect(Collectors.toList());
    }

    public CookieAssert assertExists(String cookieName) {
        Assertions.assertEquals(
                1,
                cookies.stream()
                        .filter(c -> c.getName().equals(cookieName))
                        .count()
        );

        return new CookieAssert(
                cookies.stream()
                        .filter(c -> c.getName().equals(cookieName))
                        .findFirst()
                        .orElseThrow(IllegalStateException::new)
        );
    }

    public final class CookieAssert {
        private final HttpCookie cookie;

        CookieAssert(HttpCookie cookie) {
            this.cookie = cookie;
        }

        public CookieAssert isSecure() {
            Assertions.assertTrue(cookie.getSecure());
            return this;
        }

        public CookieAssert isHttpOnly() {
            Assertions.assertTrue(cookie.isHttpOnly());
            return this;
        }
    }
}
