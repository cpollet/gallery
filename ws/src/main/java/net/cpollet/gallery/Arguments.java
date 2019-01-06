package net.cpollet.gallery;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=")
public class Arguments {
    @Parameter(names = {"-database.host"}, description = "Hostname of database")
    private String databaseHost = "localhost";

    @Override
    public String toString() {
        return "Arguments{" +
                "databaseHost='" + databaseHost + '\'' +
                '}';
    }

    String getDatabaseHost() {
        return databaseHost;
    }
}
