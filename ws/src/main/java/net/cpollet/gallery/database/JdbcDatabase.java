package net.cpollet.gallery.database;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public final class JdbcDatabase implements Database {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcDatabase(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Query query(String queryString) {
        return new JdbcQuery(jdbcTemplate, queryString);
    }
}
