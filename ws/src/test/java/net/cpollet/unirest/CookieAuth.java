package net.cpollet.unirest;

import com.mashape.unirest.request.GetRequest;
import net.cpollet.gallery.it.domain.Auth;

public final class CookieAuth implements Auth {
    private final String cookie;

    public CookieAuth(String cookie) {
        this.cookie = cookie;
    }

    @Override
    public GetRequest update(GetRequest request) {
        return request.header("cookie", "big42=" + cookie);
    }
}
