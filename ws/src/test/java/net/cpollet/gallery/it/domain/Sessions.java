package net.cpollet.gallery.it.domain;

import com.google.common.collect.ImmutableMap;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.cpollet.assertions.Assert;
import org.junit.jupiter.api.Assertions;

import java.util.Map;

public final class Sessions {
    private final String httpRoot;
    private final String root;
    private final Map<String, String> headers;

    public Sessions(String httpRoot) {
        this.httpRoot = httpRoot;
        this.root = httpRoot + "/sessions";
        this.headers = ImmutableMap.of(
                "accept", "application/json",
                "content-type", "application/json"
        );
    }

    public String create(String username, String password) throws UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.post(root)
                .basicAuth("user", "password")
                .headers(headers)
                .body(ImmutableMap.of(
                        "username", username,
                        "password", password
                ))
                .asJson();

        Assert.that(jsonResponse)
                .is201()
                .assertCookies()
                .assertExists("big42")
                .isSecure()
                .isHttpOnly();

        Assert.that(jsonResponse)
                .isJson()
                .isObject();

        String sessionId = jsonResponse.getBody().getObject().getString("sessionId");

        Assertions.assertNotNull(sessionId);
        Assertions.assertNotEquals("", sessionId);

        return sessionId;
    }
}
