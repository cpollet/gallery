package net.cpollet.gallery.it.domain;

import com.mashape.unirest.request.GetRequest;

public interface Auth {
    GetRequest update(GetRequest request);
}
