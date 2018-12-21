package net.cpollet.unirest;

import com.mashape.unirest.request.GetRequest;
import net.cpollet.gallery.it.domain.Auth;
import org.checkerframework.checker.units.qual.A;

public final class NoAuth implements Auth {
    @Override
    public GetRequest update(GetRequest request) {
        return request;
    }
}
