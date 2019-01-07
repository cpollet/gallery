package net.cpollet.gallery.database;

import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Map;

public interface Query {
    Query with(Map<String, Object> parameters);

    Query with(String key, Object value);

    <T> List<T> fetch(Class<T> targetType);

    <T> List<T> fetch(RowMapper<T> rowMapper); // FIXME replace with local interface

    void execute();
}
