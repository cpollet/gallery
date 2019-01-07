package net.cpollet.gallery;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Main {
    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(args);

        Application application = new Application(
                String.format("jdbc:postgresql://%s:5432/gallery", commandLine.databaseHost()),
                "postgres",
                "password",
                8080,
                "0.0.0.0"
        );

        application.start();
    }
}
