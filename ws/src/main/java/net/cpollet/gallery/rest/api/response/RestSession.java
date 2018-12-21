package net.cpollet.gallery.rest.api.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public final class RestSession {
    private String sessionId;
    private List<Link> links;

    public RestSession(String sessionId, List<Link> links) {
        this.sessionId = sessionId;
        this.links = links;
    }
}
