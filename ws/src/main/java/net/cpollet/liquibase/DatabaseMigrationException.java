package net.cpollet.liquibase;

final class DatabaseMigrationException extends RuntimeException {
    DatabaseMigrationException(Exception e) {
        super(e);
    }
}
