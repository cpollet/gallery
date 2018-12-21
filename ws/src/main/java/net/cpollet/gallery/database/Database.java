package net.cpollet.gallery.database;

public interface Database {
    Query query(String queryString);
}
