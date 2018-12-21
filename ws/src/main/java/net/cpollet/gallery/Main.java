package net.cpollet.gallery;

public final class Main {
    public static void main(String[] args) {
        Application application = new Application(
                "jdbc:postgresql://localhost:5432/gallery",
                "postgres",
                "password",
                8080,
                "localhost"
        );

        application.start();
    }
}
