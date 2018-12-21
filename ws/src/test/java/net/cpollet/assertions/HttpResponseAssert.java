package net.cpollet.assertions;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.junit.jupiter.api.Assertions;

public final class HttpResponseAssert {
    private final HttpResponse httpResponse;

    public HttpResponseAssert(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public HttpResponseAssert is200() {
        Assertions.assertEquals(200, httpResponse.getStatus());
        Assertions.assertEquals("OK", httpResponse.getStatusText());
        return this;
    }

    public HttpResponseAssert is201() {
        Assertions.assertEquals(201, httpResponse.getStatus());
        Assertions.assertEquals("Created", httpResponse.getStatusText());
        return this;
    }

    public HttpResponseAssert is401() {
        Assertions.assertEquals(401, httpResponse.getStatus());
        Assertions.assertEquals("Unauthorized", httpResponse.getStatusText());
        return this;
    }

    public HttpResponseAssert is404() {
        Assertions.assertEquals(404, httpResponse.getStatus());
        Assertions.assertEquals("Not Found", httpResponse.getStatusText());
        return this;
    }

    public HeadersAssert assertHeader(String name) {
        Assertions.assertTrue(httpResponse.getHeaders().containsKey(name));
        return new HeadersAssert(name, httpResponse.getHeaders().get(name));
    }

    public CookiesAssert assertCookies() {
        Assertions.assertTrue(httpResponse.getHeaders().containsKey("Set-Cookie"));
        return new CookiesAssert(httpResponse.getHeaders().get("Set-Cookie"));
    }

    public JsonNodeAssert isJson() {
        Assertions.assertTrue(httpResponse.getBody() instanceof JsonNode);
        return new JsonNodeAssert((JsonNode) httpResponse.getBody());
    }
}
