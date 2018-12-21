package net.cpollet.liquibase;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

@Slf4j
public final class LiquibaseMigration {
    private final DataSource dataSource;
    private final String migrationFile;

    public LiquibaseMigration(DataSource dataSource, String migrationFile) {
        this.dataSource = dataSource;
        this.migrationFile = migrationFile;
    }

    public LiquibaseMigration(DataSource dataSource) {
        this(dataSource, "db/migration/liquibase.xml");
    }

    public void execute() {
        log.info("Starting DB migration");
        try {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()));
            Liquibase liquibase = new liquibase.Liquibase(migrationFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (Exception e) {
            throw new DatabaseMigrationException(e);
        }
        log.info("DB migration done");
    }
}
