package net.cpollet.assertions;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.cpollet.kozan.lazy.Lazy;
import org.json.JSONArray;
import org.junit.jupiter.api.Assertions;

public final class JSONArrayAssert {
    private final JSONArray node;
    private final Lazy<DocumentContext> document;

    JSONArrayAssert(JSONArray node) {
        this.node = node;
        this.document = new Lazy<>(() -> JsonPath.parse(node.toString()));
    }

    private DocumentContext document() {
        return document.value();
    }

    public JSONArrayAssert isEmpty() {
        Assertions.assertEquals(0, node.length(), "Array is not empty");
        return this;
    }

    public JSONArrayAssert isNotEmpty() {
        Assertions.assertNotEquals(0, node.length(), "Array is empty");
        return this;
    }

    public JSONArrayAssert hasLength(int count) {
        Assertions.assertEquals(count, node.length());
        return this;
    }

    public JSONArrayAssert hasProperty(String path, String value) {
        Assertions.assertEquals(value, document().read(path, String.class));
        return this;
    }
}
