package net.cpollet.gallery.it.domain;

import com.google.common.collect.ImmutableMap;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.cpollet.assertions.Assert;
import net.cpollet.assertions.HttpResponseAssert;
import net.cpollet.assertions.JSONArrayAssert;
import net.cpollet.assertions.JSONObjectAssert;
import net.cpollet.unirest.BasicAuth;
import net.cpollet.unirest.CookieAuth;
import net.cpollet.unirest.NoAuth;

import java.util.Collections;
import java.util.Map;

public final class Albums {
    private final String httpRoot;
    private final String root;
    private final Map<String, Object> filters;
    private final Map<String, Object> sort;
    private final Map<String, String> headers;
    private final Auth auth;

    private Albums(String httpRoot, Map<String, Object> filters, Map<String, Object> sort, Map<String, String> headers, Auth auth) {
        this.httpRoot = httpRoot;
        this.root = httpRoot + "/albums";
        this.filters = filters;
        this.sort = sort;
        this.headers = headers;
        this.auth = auth;
    }

    public Albums(String httpRoot) {
        this(
                httpRoot,
                Collections.emptyMap(),
                Collections.emptyMap(),
                ImmutableMap.of(
                        "accept", "application/json",
                        "content-type", "application/json"
                ),
                new NoAuth()
        );
    }

    public Albums assertCountIs(int count) throws UnirestException {
        Assert.that(fetch(root))
                .is200()
                .isJson()
                .isArray()
                .hasLength(count);

        return this;
    }

    private HttpResponse<JsonNode> fetch(String root) throws UnirestException {
        return auth.update(Unirest.get(root))
                .headers(headers)
                .queryString(filters)
                .queryString(sort)
                .asJson();
    }

    public Albums assertCountIsUnauthorized() throws UnirestException {
        Assert.that(fetch(root))
                .is401();

        return this;
    }

    public JSONArrayAssert assertNotEmpty() throws UnirestException {
        return Assert.that(fetch(root))
                .is200()
                .isJson()
                .isArray()
                .isNotEmpty();
    }

    public Albums create(String name, String description) throws UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.post(root)
                .basicAuth("user", "password")
                .headers(headers)
                .body(ImmutableMap.of(
                        "name", name,
                        "description", description
                ))
                .asJson();

        Assert.that(jsonResponse)
                .is201()
                .isJson()
                .isObject()
                .hasProperty("name", name)
                .hasProperty("description", description)
                .hasProperty("published", false);

        long id = jsonResponse.getBody().getObject().getLong("id");

        Assert.that(jsonResponse)
                .isJson()
                .isObject()
                .hasProperty("links[0].href", path(id));

        return this;
    }

    public JSONObjectAssert assertExists(long id) throws UnirestException {
        return Assert.that(fetch(path(id)))
                .is200()
                .isJson()
                .isObject();
    }

    private String path(long id) {
        return root + "/" + id;
    }

    public Albums assertDoesNotExist(long id) throws UnirestException {
        Assert.that(fetch(path(id)))
                .is404();

        return this;
    }

    public Albums filter(String name, String value) {
        return new Albums(
                httpRoot,
                ImmutableMap.<String, Object>builder()
                        .putAll(filters)
                        .put("filter", name + ":" + value)
                        .build(),
                sort,
                headers,
                auth
        );
    }

    public Albums sort(String attribute, String order) {
        return new Albums(
                httpRoot,
                filters,
                ImmutableMap.<String, Object>builder()
                        .putAll(sort)
                        .put("sort", attribute + ":" + order)
                        .build(),
                headers,
                auth
        );
    }

    public Albums correlationId(String correlationId) {
        return new Albums(
                httpRoot,
                filters,
                sort,
                ImmutableMap.<String, String>builder()
                        .putAll(headers)
                        .put("X-Gallery-CorrelationId", correlationId)
                        .build(),
                auth
        );
    }

    public HttpResponseAssert list() throws UnirestException {
        return Assert.that(fetch(root));
    }

    public Albums withCookieAuth(String sessionCookie) {
        return new Albums(
                httpRoot,
                filters,
                sort,
                headers,
                new CookieAuth(sessionCookie)
        );
    }

    public Albums withBasicAuth(String username, String password) {
        return new Albums(
                httpRoot,
                filters,
                sort,
                headers,
                new BasicAuth(username, password)
        );
    }
}
