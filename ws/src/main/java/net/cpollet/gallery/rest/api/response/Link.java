package net.cpollet.gallery.rest.api.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class Link {
    private final String href;
    private final String type;
    private final String url;
}
