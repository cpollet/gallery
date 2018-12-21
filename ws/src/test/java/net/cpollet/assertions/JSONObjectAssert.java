package net.cpollet.assertions;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.cpollet.kozan.lazy.Lazy;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

public final class JSONObjectAssert {
    private final Lazy<DocumentContext> document;

    JSONObjectAssert(JSONObject object) {
        this.document = new Lazy<>(() -> JsonPath.parse(object.toString()));
    }

    private DocumentContext document() {
        return document.value();
    }

    public JSONObjectAssert hasProperty(String path, String value) {
        Assertions.assertEquals(value, document().read(path, String.class));
        return this;
    }

    public JSONObjectAssert hasProperty(String path, boolean value) {
        Assertions.assertEquals(value, document().read(path, Boolean.class));
        return this;
    }
}
