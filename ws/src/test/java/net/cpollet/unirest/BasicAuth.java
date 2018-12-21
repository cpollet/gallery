package net.cpollet.unirest;

import com.mashape.unirest.request.GetRequest;
import net.cpollet.gallery.it.domain.Auth;

public final class BasicAuth implements Auth {
    private final String username;
    private final String password;

    public BasicAuth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public GetRequest update(GetRequest request) {
        return request.basicAuth(username, password);
    }
}
