package net.cpollet.gallery.rest.api.response;

public final class Link {
    private final String href;
    private final String type;
    private final String url;

    public Link(String href, String type, String url) {
        this.href = href;
        this.type = type;
        this.url = url;
    }

    public String getHref() {
        return href;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}
