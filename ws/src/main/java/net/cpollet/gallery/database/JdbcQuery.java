package net.cpollet.gallery.database;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public final class JdbcQuery implements Query {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String queryString;
    private final Map<String, Object> parameters;

    private JdbcQuery(NamedParameterJdbcTemplate jdbcTemplate, String queryString, Map<String, Object> parameters) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryString = queryString;
        this.parameters = parameters;
    }

    JdbcQuery(NamedParameterJdbcTemplate jdbcTemplate, String queryString) {
        this(jdbcTemplate, queryString, Collections.emptyMap());
    }

    @Override
    public Query with(Map<String, Object> parameters) {
        return new JdbcQuery(
                jdbcTemplate,
                queryString,
                ImmutableMap.<String, Object>builder()
                        .putAll(this.parameters)
                        .putAll(parameters)
                        .build()
        );
    }

    @Override
    public Query with(String key, Object value) {
        return new JdbcQuery(
                jdbcTemplate,
                queryString,
                ImmutableMap.<String, Object>builder()
                        .putAll(this.parameters)
                        .put(key, value)
                        .build()
        );
    }

    @Override
    public <T> List<T> fetch(Class<T> targetType) {
        log();
        return jdbcTemplate.query(queryString, parameters, new SingleColumnRowMapper<>(targetType));
    }

    private void log() {
        log.debug(">> {}", queryString);
    }

    @Override
    public <T> List<T> fetch(RowMapper<T> rowMapper) {
        log();
        return jdbcTemplate.query(queryString, parameters, rowMapper);
    }

    @Override
    public void execute() {
        log();
        jdbcTemplate.update(queryString, parameters);
    }
}
