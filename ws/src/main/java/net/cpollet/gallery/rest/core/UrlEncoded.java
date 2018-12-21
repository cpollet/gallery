package net.cpollet.gallery.rest.core;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public final class UrlEncoded {
    private final String string;

    public UrlEncoded(String string) {
        this.string = string;
    }

    @SuppressWarnings("squid:S00112")
    @Override
    public String toString() {
        try {
            return URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
