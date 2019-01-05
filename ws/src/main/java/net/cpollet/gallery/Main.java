package net.cpollet.gallery;

import com.beust.jcommander.JCommander;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Main {
    public static void main(String[] args) {
        Arguments arguments = arguments(args);

        Application application = new Application(
                String.format("jdbc:postgresql://%s:5432/gallery", arguments.getDatabaseHost()),
                "postgres",
                "password",
                8080,
                "0.0.0.0"
        );

        application.start();
    }

    private static Arguments arguments(String[] args) {
        Arguments arguments = new Arguments();
        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(args);
        return arguments;
    }
}
