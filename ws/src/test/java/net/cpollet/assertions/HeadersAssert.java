package net.cpollet.assertions;

import org.junit.jupiter.api.Assertions;

import java.util.List;

public final class HeadersAssert {
    private final String name;
    private final List<String> values;

    public HeadersAssert(String name, List<String> values) {
        this.name = name;
        this.values = values;
    }

    public HeadersAssert assertEquals(String value) {
        Assertions.assertTrue(values.contains(value), String.format("[%s] not found in [%s]; found: %s", value, name, values));
        Assertions.assertEquals(1, values.size(), String.format("[%s] contains more than one element; found: %s", name, values));
        return this;
    }
}
