package net.cpollet.assertions;

import com.mashape.unirest.http.JsonNode;
import org.junit.jupiter.api.Assertions;

public final class JsonNodeAssert {
    private final JsonNode node;

    JsonNodeAssert(JsonNode node) {
        this.node = node;
    }

    public JSONArrayAssert isArray() {
        Assertions.assertTrue(node.isArray(), "Node is not an array");
        return new JSONArrayAssert(node.getArray());
    }

    public JSONObjectAssert isObject() {
        Assertions.assertTrue(!node.isArray());
        return new JSONObjectAssert(node.getObject());
    }
}
