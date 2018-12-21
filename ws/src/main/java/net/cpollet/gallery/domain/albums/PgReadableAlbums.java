package net.cpollet.gallery.domain.albums;

import net.cpollet.gallery.database.Database;
import net.cpollet.gallery.database.FilteredQuery;
import net.cpollet.gallery.database.Query;

public final class PgReadableAlbums implements FilteredQuery {
    private final Database database;
    private final String queryString;
    private final String columnFilter;

    PgReadableAlbums(Database database, String queryString, String columnFilter) {
        this.database = database;
        this.queryString = queryString;
        this.columnFilter = columnFilter;
    }

    PgReadableAlbums(Database database, String queryString) {
        this(database, queryString, "id");
    }

    @Override
    public Query toQuery() {
        return database.query(filteredQueryString());
    }

    private String filteredQueryString() {
        return String.format("SELECT * FROM (%s) AS __r WHERE __r.%s IN (%s)",
                queryString,
                columnFilter,
                "select id from albums where id <> 13" // FIXME proper implementation
        );
    }
}
