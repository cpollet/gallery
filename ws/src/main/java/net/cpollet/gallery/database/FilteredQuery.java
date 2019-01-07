package net.cpollet.gallery.database;

public interface FilteredQuery { // FIXME merge with Query?
    Query toQuery();
}
