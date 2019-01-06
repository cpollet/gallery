package net.cpollet.gallery.rest.api.response;

import java.util.List;

public final class RestSession {
    private String sessionId;
    private List<Link> links;

    public RestSession() {
        // noop
    }

    public RestSession(String sessionId, List<Link> links) {
        this.sessionId = sessionId;
        this.links = links;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}